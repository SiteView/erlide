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

import org.eclipse.jface.action.Action;
import org.erlide.eunit.EUnitPlugin;

class ShowNextFailureAction extends Action {

	private final TestRunnerViewPart fPart;

	public ShowNextFailureAction(final TestRunnerViewPart part) {
		super("Next Failure");
		setDisabledImageDescriptor(EUnitPlugin
				.getImageDescriptor("dlcl16/select_next.gif")); //$NON-NLS-1$
		setHoverImageDescriptor(EUnitPlugin
				.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
		setImageDescriptor(EUnitPlugin
				.getImageDescriptor("elcl16/select_next.gif")); //$NON-NLS-1$
		setToolTipText("Next Failed Test");
		fPart = part;
	}

	@Override
	public void run() {
		fPart.selectNextFailure();
	}
}
