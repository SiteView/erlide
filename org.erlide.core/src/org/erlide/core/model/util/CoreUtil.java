/*******************************************************************************
 * Copyright (c) 2009 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.core.model.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.erlide.core.backend.BackendCore;
import org.erlide.core.backend.BackendException;
import org.erlide.core.backend.ErlLaunchAttributes;
import org.erlide.core.backend.IBackend;
import org.erlide.core.backend.IBackendManager;
import org.erlide.core.common.CharOperation;
import org.erlide.core.common.Util;
import org.erlide.core.model.root.ErlModelException;

public final class CoreUtil {
    /**
     * Returns the given file's contents as a byte array.
     * 
     * @throws CoreException
     * @throws IOException
     */
    public static byte[] getResourceContentsAsByteArray(final IFile file)
            throws CoreException, IOException {
        InputStream stream = null;
        stream = new BufferedInputStream(file.getContents(true));
        try {
            return Util.getInputStreamAsByteArray(stream, -1);
        } finally {
            try {
                stream.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }

    /**
     * Returns the given file's contents as a character array.
     * 
     * @throws CoreException
     * @throws IOException
     * 
     * @throws ErlModelException
     */
    public static char[] getResourceContentsAsCharArray(final IFile file)
            throws IOException, CoreException {
        // Get encoding from file
        String encoding = null;
        try {
            encoding = file.getCharset();
        } catch (final CoreException ce) {
            // do not use any encoding
        }
        return getResourceContentsAsCharArray(file, encoding);
    }

    public static char[] getResourceContentsAsCharArray(final IFile file,
            final String encoding) throws IOException, CoreException {
        // Get resource contents
        InputStream stream = null;
        stream = new BufferedInputStream(file.getContents(true));
        try {
            return Util.getInputStreamAsCharArray(stream, -1, encoding);
        } finally {
            try {
                stream.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }

    /*
     * Returns the index of the most specific argument paths which is strictly
     * enclosing the path to check
     */
    public static int indexOfEnclosingPath(final IPath checkedPath,
            final IPath[] paths, final int pathCount) {

        int bestMatch = -1, bestLength = -1;
        for (int i = 0; i < pathCount; i++) {
            if (paths[i].equals(checkedPath)) {
                continue;
            }
            if (paths[i].isPrefixOf(checkedPath)) {
                final int currentLength = paths[i].segmentCount();
                if (currentLength > bestLength) {
                    bestLength = currentLength;
                    bestMatch = i;
                }
            }
        }
        return bestMatch;
    }

    /*
     * Returns the index of the first argument paths which is equal to the path
     * to check
     */
    public static int indexOfMatchingPath(final IPath checkedPath,
            final IPath[] paths, final int pathCount) {

        for (int i = 0; i < pathCount; i++) {
            if (paths[i].equals(checkedPath)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the first argument paths which is strictly nested
     * inside the path to check
     */
    public static int indexOfNestedPath(final IPath checkedPath,
            final IPath[] paths, final int pathCount) {

        for (int i = 0; i < pathCount; i++) {
            if (checkedPath.equals(paths[i])) {
                continue;
            }
            if (checkedPath.isPrefixOf(paths[i])) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean isExcluded(final IPath resourcePath,
            final char[][] exclusionPatterns) {
        return isExcluded(resourcePath, null, exclusionPatterns, false);
    }

    /**
     * Returns whether the given resource path matches one of the
     * inclusion/exclusion patterns. NOTE: should not be asked directly using
     * pkg root paths
     * 
     * @see IClasspathEntry#getInclusionPatterns
     * @see IClasspathEntry#getExclusionPatterns
     */
    private static final boolean isExcluded(final IPath resourcePath,
            final char[][] inclusionPatterns, final char[][] exclusionPatterns,
            final boolean isFolderPath) {
        if (inclusionPatterns == null && exclusionPatterns == null) {
            return false;
        }
        char[] path = resourcePath.toString().toCharArray();

        inclusionCheck: if (inclusionPatterns != null) {
            for (final char[] pattern : inclusionPatterns) {
                char[] folderPattern = pattern;
                if (isFolderPath) {
                    final int lastSlash = CharOperation.lastIndexOf('/',
                            pattern);
                    if (lastSlash != -1 && lastSlash != pattern.length - 1) { // trailing
                        // slash
                        // ->
                        // adds
                        // '**'
                        // for
                        // free
                        // (see
                        // http://ant.apache.org/manual/dirtasks.html)
                        final int star = CharOperation.indexOf('*', pattern,
                                lastSlash);
                        if (star == -1 || star >= pattern.length - 1
                                || pattern[star + 1] != '*') {
                            folderPattern = CharOperation.subarray(pattern, 0,
                                    lastSlash);
                        }
                    }
                }
                if (CharOperation.pathMatch(folderPattern, path, true, '/')) {
                    break inclusionCheck;
                }
            }
            return true; // never included
        }
        if (isFolderPath) {
            path = CharOperation.concat(path, new char[] { '*' }, '/');
        }

        return false;
    }

    private CoreUtil() {
    }

    public static IBackend getBuildOrIdeBackend(final IProject project) {
        final IBackendManager backendManager = BackendCore.getBackendManager();
        if (project != null) {
            try {
                return backendManager.getBuildBackend(project);
            } catch (final BackendException e) {
            }
        }
        return backendManager.getIdeBackend();
    }

    public static IProject[] getErlangLaunchConfigurationProjects(
            final ILaunchConfiguration configuration) throws CoreException {
        final String projectNamesString = configuration.getAttribute(
                ErlLaunchAttributes.PROJECTS, "");
        final String[] projectNames = projectNamesString.split(";");
        final List<IProject> projects = new ArrayList<IProject>();
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        for (final String s : projectNames) {
            if (s != null && s.length() > 0) {
                final IProject p = root.getProject(s);
                if (p != null) {
                    projects.add(p);
                }
            }
        }
        return projects.toArray(new IProject[projects.size()]);
    }

}
