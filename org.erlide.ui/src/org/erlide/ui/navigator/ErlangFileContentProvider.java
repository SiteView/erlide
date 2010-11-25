package org.erlide.ui.navigator;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.navigator.SaveablesProvider;
import org.eclipse.ui.progress.UIJob;
import org.erlide.core.erlang.ErlModelException;
import org.erlide.core.erlang.ErlangCore;
import org.erlide.core.erlang.IErlElement;
import org.erlide.core.erlang.IErlModel;
import org.erlide.core.erlang.IErlModelChangeListener;
import org.erlide.core.erlang.IErlModule;
import org.erlide.core.erlang.IErlProject;
import org.erlide.core.erlang.IOpenable;
import org.erlide.core.erlang.IParent;
import org.erlide.core.erlang.util.ErlideUtil;
import org.erlide.core.erlang.util.ModelUtils;
import org.erlide.jinterface.util.ErlLogger;

public class ErlangFileContentProvider implements ITreeContentProvider,
        IResourceChangeListener, IResourceDeltaVisitor,
        IErlModelChangeListener, IAdaptable {

    private static final Object[] NO_CHILDREN = new Object[0];

    StructuredViewer viewer;

    /**
     * Create the PropertiesContentProvider instance.
     * 
     * Adds the content provider as a resource change listener to track changes
     * on disk.
     * 
     */
    public ErlangFileContentProvider() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
                IResourceChangeEvent.POST_CHANGE);
        final IErlModel mdl = ErlangCore.getModel();
        mdl.addModelChangeListener(this);
    }

    /**
     * Return the model elements for a *.erl IFile or NO_CHILDREN for otherwise.
     */
    public Object[] getChildren(Object parentElement) {
        try {
            if (parentElement instanceof IFile) {
                parentElement = ModelUtils.getModule((IFile) parentElement);
            }
            if (parentElement instanceof IOpenable) {
                final IOpenable openable = (IOpenable) parentElement;
                openable.open(null);
            }
            if (parentElement instanceof IParent) {
                final IParent parent = (IParent) parentElement;
                final Collection<IErlElement> children = parent.getChildren();
                return children.toArray();
            }
        } catch (final ErlModelException e) {
            ErlLogger.warn(e);
        }
        return NO_CHILDREN;
    }

    /**
     * Load the model from the given file, if possible.
     * 
     * @param modelFile
     *            The IFile which contains the persisted model
     */

    public Object getParent(final Object element) {
        if (element instanceof IErlElement) {
            final IErlElement elt = (IErlElement) element;
            final IErlElement parent = elt.getParent();
            if (parent instanceof IErlModule || parent instanceof IErlProject) {
                try {
                    return parent.getCorrespondingResource();
                } catch (final ErlModelException e) {
                    ErlLogger.warn(e);
                }
            }
        }
        return null;
    }

    public boolean hasChildren(final Object element) {
        if (element instanceof IFile || element instanceof IErlModule) {
            // it was too slow to open all modules to find out;
            // empty modules aren't widely used anyway :-)
            return true;
        } else if (element instanceof IParent) {
            final IParent parent = (IParent) element;
            return parent.hasChildren();
        }
        return false;
    }

    public Object[] getElements(final Object inputElement) {
        return getChildren(inputElement);
    }

    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        ErlangCore.getModel().removeModelChangeListener(this);
    }

    public void inputChanged(final Viewer viewer, final Object oldInput,
            final Object newInput) {
        if (viewer instanceof StructuredViewer) {
            this.viewer = (StructuredViewer) viewer;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org
     * .eclipse.core.resources.IResourceChangeEvent)
     */
    public void resourceChanged(final IResourceChangeEvent event) {
        final IResourceDelta delta = event.getDelta();
        try {
            if (delta != null) {
                // ErlLogger.debug("change " + event.toString());
                delta.accept(this);
            }
        } catch (final CoreException e) {
            ErlLogger.warn(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core
     * .resources.IResourceDelta)
     */
    public boolean visit(final IResourceDelta delta) {

        final IResource source = delta.getResource();
        switch (source.getType()) {
        case IResource.ROOT:
        case IResource.PROJECT:
        case IResource.FOLDER:
            return true;
        case IResource.FILE:
            final IFile file = (IFile) source;
            if (ErlideUtil.hasModuleExtension(file.getName())) {
                doRefresh(file);
            }
            return false;
        }
        return false;
    }

    private void doRefresh(final IFile file) {
        final String title = "Update Erlang Model in CommonViewer: "
                + file.getName();
        new UIJob(title) {
            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                if (viewer != null && !viewer.getControl().isDisposed()) {
                    viewer.refresh(file);
                }
                return Status.OK_STATUS;
            }
        }.schedule();
    }

    public void elementChanged(final IErlElement element) {
        if (element instanceof IErlModule) {
            final IErlModule m = (IErlModule) element;
            final IResource r = m.getResource();
            if (r instanceof IFile) {
                doRefresh((IFile) r);
            }
        }
    }

    public Object getAdapter(@SuppressWarnings("rawtypes") final Class required) {
        if (SaveablesProvider.class.equals(required)) {
            return new SaveablesProvider() {

                @Override
                public Object[] getElements(final Saveable saveable) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Saveable getSaveable(final Object element) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Saveable[] getSaveables() {
                    // TODO Auto-generated method stub
                    return null;
                }
            };
        }
        return null;
    }
}
