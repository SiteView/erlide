/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.erlide.ui.editors.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.erlide.core.erlang.ErlModelException;
import org.erlide.core.erlang.IErlElement;
import org.erlide.core.erlang.IErlModule;
import org.erlide.core.erlang.ISourceRange;
import org.erlide.jinterface.util.ErlLogger;
import org.erlide.ui.ErlideUIPlugin;
import org.erlide.ui.editors.erl.ErlangEditor;

/**
 * A number of routines for working with JavaElements in editors.
 * 
 * Use 'isOpenInEditor' to test if an element is already open in a editor Use
 * 'openInEditor' to force opening an element in a editor With 'getWorkingCopy'
 * you get the working copy (element in the editor) of an element
 */
public class EditorUtility {

    public static boolean isEditorInput(final Object element,
            final IEditorPart editor) {
        if (editor != null) {
            try {
                return editor.getEditorInput().equals(getEditorInput(element));
            } catch (final ErlModelException e) {
                ErlLogger.warn(e);
            }
        }
        return false;
    }

    /**
     * Tests if a CU is currently shown in an editor
     * 
     * @return the IEditorPart if shown, null if element is not open in an
     *         editor
     */
    public static IEditorPart isOpenInEditor(final Object inputElement) {
        IEditorInput input = null;

        try {
            input = getEditorInput(inputElement);
        } catch (final ErlModelException e) {
            ErlLogger.warn(e);
        }

        if (input != null) {
            final IWorkbenchPage p = ErlideUIPlugin.getActivePage();
            if (p != null) {
                return p.findEditor(input);
            }
        }

        return null;
    }

    /**
     * Opens an Erlang editor for an element (IErlElement, IFile, IStorage...)
     * 
     * @return the IEditorPart or null if wrong element type or opening failed
     */
    public static ITextEditor openInEditor(final Object inputElement,
            final boolean activate) throws ErlModelException, PartInitException {

        if (inputElement instanceof IFile) {
            return openInEditor((IFile) inputElement, activate);
        }

        final IEditorInput input = getEditorInput(inputElement);
        if (input != null) {
            return openInEditor(input, getEditorID(input, inputElement),
                    activate);
        }

        return null;
    }

    /**
     * Selects a Erlang Element in an editor
     */
    public static boolean revealInEditor(final IEditorPart part,
            final IErlElement element) {
        if (element != null && part instanceof ErlangEditor) {
            ((ErlangEditor) part).setSelection(element);
            return true;
        }
        return false;
    }

    /**
     * Selects and reveals the given region in the given editor part.
     */
    public static void revealInEditor(final IEditorPart part,
            final IRegion region) {
        if (part != null && region != null) {
            revealInEditor(part, region.getOffset(), region.getLength());
        }
    }

    public static void revealInEditor(final IEditorPart part,
            final ISourceRange sourceRange) {
        if (part != null && sourceRange != null) {
            final int offset = sourceRange.getOffset();
            final int length = sourceRange.getLength();
            revealInEditor(part, offset, length);
        }
    }

    /**
     * Selects and reveals the given offset and length in the given editor part.
     */
    public static void revealInEditor(final IEditorPart editor,
            final int offset, final int length) {
        if (editor instanceof ITextEditor) {
            ((ITextEditor) editor).selectAndReveal(offset, length);
            return;
        }

        // Support for non-text editor - try IGotoMarker interface
        if (editor instanceof IGotoMarker) {
            final IEditorInput input = editor.getEditorInput();
            if (input instanceof IFileEditorInput) {
                final IGotoMarker gotoMarkerTarget = (IGotoMarker) editor;
                final WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

                    @Override
                    protected void execute(final IProgressMonitor monitor)
                            throws CoreException {
                        IMarker marker = null;
                        try {
                            marker = ((IFileEditorInput) input).getFile()
                                    .createMarker(IMarker.TEXT);
                            marker.setAttribute(IMarker.CHAR_START, offset);
                            marker.setAttribute(IMarker.CHAR_END, offset
                                    + length);

                            gotoMarkerTarget.gotoMarker(marker);

                        } finally {
                            if (marker != null) {
                                marker.delete();
                            }
                        }
                    }
                };

                try {
                    op.run(null);
                } catch (final InvocationTargetException ex) {
                    // reveal failed
                } catch (final InterruptedException e) {
                    Assert.isTrue(false, "this operation can not be canceled"); //$NON-NLS-1$
                }
            }
            return;
        }

        /*
         * Workaround: send out a text selection XXX: Needs to be improved, see
         * https://bugs.eclipse.org/bugs/show_bug.cgi?id=32214
         */
        if (editor != null
                && editor.getEditorSite().getSelectionProvider() != null) {
            final IEditorSite site = editor.getEditorSite();
            if (site == null) {
                return;
            }

            final ISelectionProvider provider = editor.getEditorSite()
                    .getSelectionProvider();
            if (provider == null) {
                return;
            }

            provider.setSelection(new TextSelection(offset, length));
        }
    }

    private static ITextEditor openInEditor(final IFile file,
            final boolean activate) throws PartInitException {
        if (file != null) {
            final IWorkbenchPage p = ErlideUIPlugin.getActivePage();
            if (p != null) {
                final IEditorPart editorPart = IDE
                        .openEditor(p, file, activate);
                if (editorPart instanceof ITextEditor) {
                    return (ITextEditor) editorPart;
                }
            }
        }
        return null;
    }

    private static ITextEditor openInEditor(final IEditorInput input,
            final String editorID, final boolean activate)
            throws PartInitException {
        if (input != null) {
            final IWorkbenchPage p = ErlideUIPlugin.getActivePage();
            if (p != null) {
                final IEditorPart editorPart = p.openEditor(input, editorID,
                        activate);
                if (editorPart instanceof ITextEditor) {
                    return (ITextEditor) editorPart;
                }
            }
        }
        return null;
    }

    public static String getEditorID(final IEditorInput input,
            final Object inputObject) {
        IEditorDescriptor editorDescriptor;
        try {
            if (input instanceof IFileEditorInput) {
                editorDescriptor = IDE
                        .getEditorDescriptor(((IFileEditorInput) input)
                                .getFile());
            } else {
                editorDescriptor = IDE.getEditorDescriptor(input.getName());
            }
        } catch (final PartInitException e) {
            return null;
        }

        if (editorDescriptor != null) {
            return editorDescriptor.getId();
        }

        return null;
    }

    private static IEditorInput getEditorInput(IErlElement element)
            throws ErlModelException {
        final IResource resource = element.getResource();
        if (resource instanceof IFile) {
            return new FileEditorInput((IFile) resource);
        }
        if (!(element instanceof IErlModule)) {
            element = element.getParent();
        }
        if (element.getFilePath() != null) {
            final IPath path = new Path(element.getFilePath());
            IFileStore fileStore = EFS.getLocalFileSystem().getStore(
                    path.removeLastSegments(1));
            fileStore = fileStore.getChild(path.lastSegment());
            final IFileInfo fetchInfo = fileStore.fetchInfo();
            if (!fetchInfo.isDirectory() && fetchInfo.exists()) {
                return new FileStoreEditorInput(fileStore);
            }
        }
        return null;
    }

    public static IEditorInput getEditorInput(final Object input)
            throws ErlModelException {
        if (input instanceof IErlElement) {
            return getEditorInput((IErlElement) input);
        }

        if (input instanceof IFile) {
            return new FileEditorInput((IFile) input);
        }

        // if (input instanceof IStorage)
        // return new JarEntryEditorInput((IStorage)input);

        return null;
    }

    /**
     * Opens the editor on the given element and subsequently selects it.
     */
    public static void openElementInEditor(final Object element,
            final boolean activate) throws ErlModelException, PartInitException {
        final IEditorPart part = EditorUtility.openInEditor(element, activate);
        if (element instanceof IErlElement) {
            EditorUtility.revealInEditor(part, (IErlElement) element);
        }
    }

    /**
     * If the current active editor edits a erlang element return it, else
     * return null
     */
    public static IErlElement getActiveEditorErlangInput() {
        final IWorkbenchPage page = ErlideUIPlugin.getActivePage();
        if (page != null) {
            final IEditorPart part = page.getActiveEditor();
            if (part != null) {
                final IEditorInput editorInput = part.getEditorInput();
                if (editorInput != null) {
                    return (IErlElement) editorInput
                            .getAdapter(IErlElement.class);
                    // return JavaUI.getEditorInputJavaElement(editorInput);
                }
            }
        }
        return null;
    }

    /**
     * Maps the localized modifier name to a code in the same manner as
     * #findModifier.
     * 
     * @param modifierName
     *            the modifier name
     * @return the SWT modifier bit, or <code>0</code> if no match was found
     * @since 2.1.1
     */
    public static int findLocalizedModifier(final String modifierName) {
        if (modifierName == null) {
            return 0;
        }

        if (modifierName.equalsIgnoreCase(Action.findModifierString(SWT.CTRL))) {
            return SWT.CTRL;
        }
        if (modifierName.equalsIgnoreCase(Action.findModifierString(SWT.SHIFT))) {
            return SWT.SHIFT;
        }
        if (modifierName.equalsIgnoreCase(Action.findModifierString(SWT.ALT))) {
            return SWT.ALT;
        }
        if (modifierName.equalsIgnoreCase(Action
                .findModifierString(SWT.COMMAND))) {
            return SWT.COMMAND;
        }

        return 0;
    }

}
