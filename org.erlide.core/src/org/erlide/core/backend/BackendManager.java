/*******************************************************************************
 * Copyright (c) 2005 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.core.backend;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.erlide.backend.Backend;
import org.erlide.backend.BackendCore;
import org.erlide.backend.BackendException;
import org.erlide.backend.BackendPlugin;
import org.erlide.backend.BackendUtil;
import org.erlide.backend.CodeBundle;
import org.erlide.backend.CodeBundle.CodeContext;
import org.erlide.backend.ErlLaunchAttributes;
import org.erlide.backend.ErtsProcess;
import org.erlide.backend.IBackendListener;
import org.erlide.backend.epmd.EpmdWatchJob;
import org.erlide.backend.rpc.RpcCallSite;
import org.erlide.backend.runtime.RuntimeInfo;
import org.erlide.backend.util.MessageReporter;
import org.erlide.backend.util.MessageReporter.ReporterPosition;
import org.erlide.backend.util.Tuple;
import org.erlide.core.backend.internal.ManagedLauncher;
import org.erlide.core.erlang.ErlangCore;
import org.erlide.core.erlang.IErlProject;
import org.erlide.core.erlang.util.ErlideUtil;
import org.erlide.jinterface.epmd.EpmdWatcher;
import org.erlide.jinterface.epmd.IEpmdListener;
import org.erlide.jinterface.util.ErlLogger;
import org.osgi.framework.Bundle;

import com.ericsson.otp.erlang.OtpNodeStatus;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class BackendManager extends OtpNodeStatus implements
        IEpmdListener {

    public static final String DEFAULT_VERSION = "R13B";
    public static final String[] SUPPORTED_MAIN_VERSIONS = new String[] { "",
            "R12B", "R13B", "R14A" };
    public static final String[] SUPPORTED_VERSIONS = new String[] { "",
            "R12B-1", "R12B-2", "R12B-3", "R12B-4", "R12B-5", "R13B", "R14A" };

    public enum BackendEvent {
        ADDED, REMOVED, MODULE_LOADED
    }

    public enum BackendOptions {
        DEBUG, AUTOSTART, TRAP_EXIT, NO_CONSOLE, INTERNAL, IDE, LOAD_ALL_NODES
    }

    private volatile ErlideBackend ideBackend;
    private final Object ideBackendLock = new Object();
    private final Map<IProject, Set<ErlideBackend>> executionBackends;
    private final Map<String, ErlideBackend> buildBackends;
    final List<IBackendListener> listeners;
    private final Map<Bundle, CodeBundle> codeBundles;

    private final EpmdWatcher epmdWatcher;
    private final Set<ErlideBackend> allBackends;

    @SuppressWarnings("synthetic-access")
    private static final class LazyBackendManagerHolder {
        public static final BackendManager instance = new BackendManager();
    }

    public static final BackendManager getDefault() {
        return LazyBackendManagerHolder.instance;
    }

    private BackendManager() {
        ideBackend = null;
        executionBackends = new HashMap<IProject, Set<ErlideBackend>>();
        buildBackends = new HashMap<String, ErlideBackend>();
        allBackends = Sets.newHashSet();
        listeners = Lists.newArrayList();
        codeBundles = Maps.newHashMap();

        // ManagedLauncher.startEpmdProcess();
        epmdWatcher = new EpmdWatcher();
        epmdWatcher.addEpmdListener(this);
        new EpmdWatchJob(epmdWatcher).schedule(100);

        // TODO remove this when all users have cleaned up
        cleanupInternalLCs();
    }

    public ErlideBackend createBackend(final RuntimeInfo info,
            final Set<BackendOptions> options, final ILaunch launch,
            final Map<String, String> env) throws BackendException {
        final String nodeName = info.getNodeName();
        final boolean exists = EpmdWatcher.findRunningNode(nodeName);
        ErlideBackend b = null;

        final boolean isRemoteNode = nodeName.contains("@");
        boolean watch = true;
        if (exists || isRemoteNode) {
            ErlLogger.debug("create standalone " + options + " backend '"
                    + info + "' " + Thread.currentThread());
            b = new ErlideBackend(info);
            watch = false;
        } else if (options.contains(BackendOptions.AUTOSTART)) {
            ErlLogger.debug("create managed " + options + " backend '" + info
                    + "' " + Thread.currentThread());
            b = new ErlideBackend(info);

            final ManagedLauncher launcher = new ManagedLauncher(launch, info,
                    env);
            final IStreamsProxy streamsProxy = launcher.getStreamsProxy();
            b.setStreamsProxy(streamsProxy);
            b.setManaged(true);
        }
        if (b == null) {
            ErlLogger.error("Node %s not found, could not launch!", nodeName);
            return null;
        }
        addBackend(b);
        b.setLaunch(launch);
        if (launch != null) {
            DebugPlugin.getDefault().getLaunchManager().addLaunchListener(b);
        }
        try {
            initializeBackend(options, b, watch);
        } catch (final IOException e) {
            ErlLogger.error(e);
            // throw new BackendException(e);
        }
        return b;
    }

    private void addBackend(final ErlideBackend b) {
        synchronized (allBackends) {
            allBackends.add(b);
        }
    }

    private void initializeBackend(final Set<BackendOptions> options,
            final ErlideBackend b, final boolean watchNode) throws IOException {
        b.initializeRuntime();
        if (b.isDistributed()) {
            b.connect();
            for (final CodeBundle bb : codeBundles.values()) {
                b.register(bb);
            }
            final boolean monitorNode = options.contains(BackendOptions.IDE)
                    && "true".equals(System.getProperty("erlide.monitor.ide"));
            b.initErlang(monitorNode, watchNode);
            b.registerStatusHandler(this);
            b.setDebug(options.contains(BackendOptions.DEBUG));
            b.setTrapExit(options.contains(BackendOptions.TRAP_EXIT));
        }
        notifyBackendChange(b, BackendEvent.ADDED, null, null);
    }

    private ErlideBackend createInternalBackend(final RuntimeInfo info,
            final Set<BackendOptions> options, final Map<String, String> env)
            throws BackendException {
        final ILaunchConfiguration launchConfig = getLaunchConfiguration(info,
                options);
        ILaunch launch;
        try {
            launch = launchConfig.launch(ILaunchManager.RUN_MODE,
                    new NullProgressMonitor(), false, false);
        } catch (final CoreException e) {
            e.printStackTrace();
            return null;
        }
        final ErlideBackend b = createBackend(info, options, launch, env);
        return b;
    }

    public ErlideBackend getBuildBackend(final IProject project)
            throws BackendException {
        final IErlProject erlProject = ErlangCore.getModel().getErlangProject(
                project);
        final RuntimeInfo info = erlProject.getRuntimeInfo();
        if (info == null) {
            ErlLogger.info("Project %s has no runtime info, using ide",
                    project.getName());
            if (ideBackend == null) {
                throw new BackendException(
                        "IDE backend is not created - check configuration!");
            }
            ideBackend.addProjectPath(project);
            return ideBackend;
        }
        final String version = info.getVersion().asMajor().toString();
        ErlideBackend b = buildBackends.get(version);
        if (b == null) {
            info.setNodeName(version);
            info.setNodeNameSuffix("_"
                    + org.erlide.backend.util.BackendUtils.getErlideNameTag());
            info.setCookie("erlide");
            info.setHasConsole(false);
            // will add workspace unique id
            final EnumSet<BackendOptions> options = EnumSet.of(
                    BackendOptions.AUTOSTART, BackendOptions.NO_CONSOLE,
                    BackendOptions.INTERNAL);
            b = createInternalBackend(info, options, null);
            buildBackends.put(version, b);
        }
        return b;
    }

    private ILaunchConfiguration getLaunchConfiguration(final RuntimeInfo info,
            final Set<BackendOptions> options) {
        final ILaunchManager manager = DebugPlugin.getDefault()
                .getLaunchManager();
        final ILaunchConfigurationType type = manager
                .getLaunchConfigurationType(ErtsProcess.CONFIGURATION_TYPE_INTERNAL);
        ILaunchConfigurationWorkingCopy workingCopy;
        try {
            final String name = getLaunchName(info, options);
            workingCopy = type.newInstance(null, name);
            workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,
                    "ISO-8859-1");
            workingCopy.setAttribute(ErlLaunchAttributes.NODE_NAME,
                    info.getNodeName());
            workingCopy.setAttribute(ErlLaunchAttributes.RUNTIME_NAME,
                    info.getName());
            workingCopy.setAttribute(ErlLaunchAttributes.COOKIE,
                    info.getCookie());
            workingCopy.setAttribute(ErlLaunchAttributes.CONSOLE,
                    !options.contains(BackendOptions.NO_CONSOLE));
            workingCopy.setAttribute(ErlLaunchAttributes.INTERNAL,
                    options.contains(BackendOptions.INTERNAL));
            if (System.getProperty("erlide.internal.shortname", "false")
                    .equals("true")) {
                workingCopy.setAttribute(ErlLaunchAttributes.USE_LONG_NAME,
                        false);
                info.useLongName(false);
            }
            return workingCopy;
        } catch (final CoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLaunchName(final RuntimeInfo info,
            final Set<BackendOptions> options) {
        return "internal_" + info.getNodeName();
    }

    public synchronized Set<ErlideBackend> getExecutionBackends(
            final IProject project) {
        final Set<ErlideBackend> bs = executionBackends.get(project);
        if (bs == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(bs);
    }

    public ErlideBackend getIdeBackend() {
        // System.out.println("GET ide" + Thread.currentThread());
        if (ideBackend == null) {
            synchronized (ideBackendLock) {
                if (ideBackend == null) {
                    try {
                        createIdeBackend();
                    } catch (final BackendException e) {
                        final String msg = "Could not start IDE backend: "
                                + e.getMessage();
                        MessageReporter.show(msg, ReporterPosition.MODAL);
                        ErlLogger.error(msg);
                    }
                }
            }
        }
        // System.out.println(">>> " + ideBackend);
        return ideBackend;
    }

    private void createIdeBackend() throws BackendException {
        final RuntimeInfo info = RuntimeInfo.copy(BackendCore
                .getRuntimeInfoManager().getErlideRuntime(), false);
        if (info != null) {
            final String defLabel = BackendUtil.getLabelProperty();
            if (defLabel != null) {
                info.setNodeName(defLabel);
            } else {
                final String nodeName = org.erlide.backend.util.BackendUtils
                        .getErlideNameTag() + "_erlide";
                info.setNodeName(nodeName);
            }
            info.setCookie("erlide");
            info.setHasConsole(ErlideUtil.isDeveloper());
            ErlLogger.debug("creating IDE backend %s", info.getName());
            final EnumSet<BackendOptions> options = EnumSet.of(
                    BackendOptions.AUTOSTART, BackendOptions.INTERNAL,
                    BackendOptions.IDE);
            if (!ErlideUtil.isDeveloper()) {
                options.add(BackendOptions.NO_CONSOLE);
            }
            ideBackend = createInternalBackend(info, options, null);
        } else {
            ErlLogger.error("There is no erlideRuntime defined! "
                    + "Could not start IDE backend.");
        }
    }

    public void addBackendListener(final IBackendListener listener) {
        listeners.add(listener);
    }

    public void removeBackendListener(final IBackendListener listener) {
        listeners.remove(listener);
    }

    public Collection<ErlideBackend> getAllBackends() {
        synchronized (allBackends) {
            return Collections.unmodifiableCollection(allBackends);
        }
    }

    private void addCodeBundle(final IExtension extension) {
        final String pluginId = extension.getContributor().getName();
        final Bundle plugin = Platform.getBundle(pluginId);

        final List<Tuple<String, CodeContext>> paths = Lists.newArrayList();
        Tuple<String, String> init = null;
        for (final IConfigurationElement el : extension
                .getConfigurationElements()) {
            if ("beam_dir".equals(el.getName())) {
                final String dir = el.getAttribute("path");
                final String t = el.getAttribute("context").toUpperCase();
                final CodeContext type = Enum.valueOf(CodeContext.class, t);
                paths.add(new Tuple<String, CodeContext>(dir, type));
            } else if ("init".equals(el.getName())) {
                final String module = el.getAttribute("module");
                final String function = el.getAttribute("function");
                init = new Tuple<String, String>(module, function);
            } else {
                ErlLogger
                        .error("Unknown code bundle element: %s", el.getName());
            }
        }
        addBundle(plugin, paths, init);
    }

    public void addBundle(final Bundle b,
            final Collection<Tuple<String, CodeContext>> paths,
            final Tuple<String, String> init) {
        final CodeBundle p = findBundle(b);
        if (p != null) {
            return;
        }
        final CodeBundle pp = new CodeBundle(b, paths, init);
        codeBundles.put(b, pp);
        forEachBackend(new ErlideBackendVisitor() {
            public void visit(final ErlideBackend bb) {
                bb.register(pp);
            }
        });
    }

    private CodeBundle findBundle(final Bundle b) {
        return codeBundles.get(b);
    }

    public void forEachBackend(final ErlideBackendVisitor visitor) {
        for (final ErlideBackend b : getAllBackends()) {
            visitor.visit(b);
        }
    }

    public synchronized void updateNodeStatus(final String host,
            final Collection<String> started, final Collection<String> stopped) {
        for (final String b : started) {
            final String name = b + "@" + host;
            // ErlLogger.debug("(epmd) started: '%s'", name);
            remoteStatus(name, true, null);
        }
        for (final String b : stopped) {
            final String name = b + "@" + host;
            // ErlLogger.debug("(epmd) stopped: '%s'", name);
            remoteStatus(name, false, null);
        }

    }

    public synchronized void addExecutionBackend(final IProject project,
            final ErlideBackend b) {
        Set<ErlideBackend> list = executionBackends.get(project);
        if (list == null) {
            list = new HashSet<ErlideBackend>();
            executionBackends.put(project, list);
        }
        list.add(b);
        b.addProjectPath(project);
    }

    public synchronized void removeExecutionBackend(final IProject project,
            final RpcCallSite b) {
        Set<ErlideBackend> list = executionBackends.get(project);
        if (list == null) {
            list = new HashSet<ErlideBackend>();
            executionBackends.put(project, list);
        }
        list.remove(b);
    }

    public EpmdWatcher getEpmdWatcher() {
        return epmdWatcher;
    }

    private void remoteNodeStatus(final String node, final boolean up,
            final Object info) {
        if (!up) {
            for (final Entry<IProject, Set<ErlideBackend>> e : executionBackends
                    .entrySet()) {
                for (final Backend be : e.getValue()) {
                    final String bnode = be.getInfo().getNodeName();
                    if (BackendUtil.buildLocalNodeName(bnode, true)
                            .equals(node)) {
                        removeExecutionBackend(e.getKey(), be);
                        break;
                    }
                }
            }
        }
    }

    public void dispose(final ErlideBackend backend) {
        if (backend != null && backend != ideBackend) {
            backend.dispose();
        }
    }

    @Override
    public void remoteStatus(final String node, final boolean up,
            final Object info) {
        // final String dir = up ? "up" : "down";
        // ErlLogger.debug(String.format("@@: %s %s %s", node, dir, info));
        remoteNodeStatus(node, up, info);
    }

    void notifyBackendChange(final Backend b, final BackendEvent type,
            final IProject project, final String moduleName) {
        if (listeners == null) {
            return;
        }

        final Object[] copiedListeners = listeners.toArray();
        for (final Object element : copiedListeners) {
            final IBackendListener listener = (IBackendListener) element;
            switch (type) {
            case ADDED:
                listener.runtimeAdded(b);
                break;
            case REMOVED:
                listener.runtimeRemoved(b);
                break;
            case MODULE_LOADED:
                listener.moduleLoaded(b, project, moduleName);
            }
        }
    }

    public void loadCodepathExtensions() {
        final IExtensionPoint exPnt = BackendPlugin.getCodepathExtension();
        // TODO listen to changes to the registry!

        final IExtension[] extensions = exPnt.getExtensions();
        for (int e = 0; e < extensions.length; e++) {
            final IExtension extension = extensions[e];
            if (!extension.isValid()) {
                continue;
            }
            ErlangCore.getBackendManager().addCodeBundle(extension);
        }
    }

    public Backend getByName(final String nodeName) {
        final Collection<ErlideBackend> list = getAllBackends();
        for (final Backend b : list) {
            if (b.getName().equals(nodeName)) {
                return b;
            }
        }
        return null;
    }

    private void cleanupInternalLCs() {
        final ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
        try {
            final ILaunchConfiguration[] cfgs = lm.getLaunchConfigurations();
            int n = 0;
            for (final ILaunchConfiguration cfg : cfgs) {
                final String name = cfg.getName();
                if (name.startsWith("internal")) {
                    @SuppressWarnings("deprecation")
                    final IPath path = cfg.getLocation();
                    final File file = new File(path.toString());
                    file.delete();
                    n++;
                }
            }
            ErlLogger.debug("Cleaned up %d old LCs", n);
        } catch (final Exception e) {
            // ignore
        }
    }

    public void moduleLoaded(final Backend b, final IProject project,
            final String moduleName) {
        notifyBackendChange(b, BackendEvent.MODULE_LOADED, project, moduleName);
    }

    public ErlideBackend getBackendForLaunch(final ILaunch launch) {
        for (final ErlideBackend backend : allBackends) {
            if (backend.getLaunch() == launch) {
                return backend;
            }
        }
        return null;
    }
}
