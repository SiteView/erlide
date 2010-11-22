/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.erlide.ui.eunit.internal.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.erlide.ui.eunit.internal.model.TestElement;
import org.erlide.ui.eunit.internal.model.TestRoot;
import org.erlide.ui.eunit.internal.model.TestSuiteElement;

public class TestSessionTreeContentProvider implements ITreeContentProvider {

	private final Object[] NO_CHILDREN = new Object[0];

	public void dispose() {
	}

	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof TestSuiteElement) {
			return ((TestSuiteElement) parentElement).getChildren();
		} else {
			return NO_CHILDREN;
		}
	}

	public Object[] getElements(final Object inputElement) {
		return ((TestRoot) inputElement).getChildren();
	}

	public Object getParent(final Object element) {
		return ((TestElement) element).getParent();
	}

	public boolean hasChildren(final Object element) {
		if (element instanceof TestSuiteElement) {
			return ((TestSuiteElement) element).getChildren().length != 0;
		} else {
			return false;
		}
	}

	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
	}
}
