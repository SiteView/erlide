/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.erlide.core.erlang;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.erlide.core.erlang.IErlElement.Kind;

/**
 * Common protocol for Erlang elements that contain other Erlang elements.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface IParent {

    /**
     * Returns the immediate children of this element. Unless otherwise
     * specified by the implementing element, the children are in no particular
     * order.
     * 
     * @exception ErlModelException
     *                if this element does not exist or if an exception occurs
     *                while accessing its corresponding resource
     * @return the immediate children of this element
     */
    Collection<IErlElement> getChildren() throws ErlModelException;

    /**
     * Returns whether this element has one or more immediate children. This is
     * a convenience method, and may be more efficient than testing whether
     * <code>getChildren</code> is an empty array.
     * 
     * @exception ErlModelException
     *                if this element does not exist or if an exception occurs
     *                while accessing its corresponding resource
     * @return true if the immediate children of this element, false otherwise
     */
    boolean hasChildren();

    Collection<IErlElement> getChildrenOfKind(Kind kind)
            throws ErlModelException;

    boolean hasChildrenOfKind(Kind kind);

    void addChild(IErlElement child);

    void removeChild(IErlElement e);

    IErlElement getChildNamed(String s);

    IErlElement getChildWithResource(IResource rsrc);
}
