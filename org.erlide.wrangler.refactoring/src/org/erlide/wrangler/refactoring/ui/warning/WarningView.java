/*******************************************************************************
 * Copyright (c) 2010 György Orosz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     György Orosz - initial API and implementation
 ******************************************************************************/
package org.erlide.wrangler.refactoring.ui.warning;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * Displays warning messages got from Wrangler
 * 
 * @author Gyorgy Orosz
 * @version %I%, %G%
 */
public class WarningView extends ViewPart implements IWarningHandler {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.erlide.wrangler.refactoring.ui.warning.WarningView";

	private TableViewer viewer;

	private ArrayList<WarningMessage> warningMessages = new ArrayList<WarningMessage>();

	protected final class RemoveAction extends Action {
		protected RemoveAction() {
			setText("Remove all");
			setToolTipText("Remove all warning message");
			setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL));

		}

		@Override
		public void run() {
			removeAll();
		}
	}

	// private Action doubleClickAction;

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return warningMessages.toArray();
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(((WarningMessage) obj).getMessage());
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJS_WARN_TSK);
		}
	}

	class NameSorter extends ViewerSorter {
		@Override
		public int compare(Viewer theViewer, Object e1, Object e2) {
			WarningMessage w1 = (WarningMessage) e1;
			WarningMessage w2 = (WarningMessage) e2;
			return -1 * w1.getTimestamp().compareTo(w2.getTimestamp());
		}
	}

	/**
	 * The constructor.
	 */
	public WarningView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new RemoveAction());

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void addMessage(String message) {
		warningMessages.add(new WarningMessage(message));
	}

	public void refresh() {
		try {
			viewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void removeAll() {
		warningMessages.clear();
		this.refresh();
	}
}