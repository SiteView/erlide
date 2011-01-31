/*******************************************************************************
 * Copyright (c) 2008 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.jinterface.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangException;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.SignatureException;
import com.google.common.collect.Lists;

public final class Bindings {

    private final Map<String, OtpErlangObject> bindings;

    public Bindings() {
        bindings = new HashMap<String, OtpErlangObject>();
    }

    public Bindings(final Bindings other) {
        this();
        merge(other);
    }

    public void merge(final Bindings other) {
        bindings.putAll(other.bindings);
    }

    public OtpErlangObject get(final String name) {
        return bindings.get(name);
    }

    public int getInt(final String name) throws OtpErlangException {
        final OtpErlangObject r = get(name);
        if (r instanceof OtpErlangLong) {
            return ((OtpErlangLong) r).intValue();
        }
        throw new OtpErlangException("value is not an integer");
    }

    public long getLong(final String name) throws OtpErlangException {
        final OtpErlangObject r = get(name);
        if (r instanceof OtpErlangLong) {
            return ((OtpErlangLong) r).longValue();
        }
        throw new OtpErlangException("value is not an integer");
    }

    public String getAtom(final String name) throws OtpErlangException {
        final OtpErlangObject r = get(name);
        if (r instanceof OtpErlangAtom) {
            return ((OtpErlangAtom) r).atomValue();
        }
        throw new OtpErlangException("value is not an atom");
    }

    public String getString(final String name) throws OtpErlangException {
        final OtpErlangObject r = get(name);
        if (r instanceof OtpErlangString) {
            return ((OtpErlangString) r).stringValue();
        }
        throw new OtpErlangException("value is not a string");
    }

    public Collection<OtpErlangObject> getList(final String name)
            throws OtpErlangException {
        final OtpErlangObject r = get(name);
        if (r instanceof OtpErlangList) {
            return Lists.newArrayList(((OtpErlangList) r).elements());
        }
        throw new OtpErlangException("value is not a list");
    }

    public OtpErlangObject[] getTuple(final String name)
            throws OtpErlangException {
        final OtpErlangObject r = get(name);
        if (r instanceof OtpErlangTuple) {
            return ((OtpErlangTuple) r).elements();
        }
        throw new OtpErlangException("value is not a tuple");
    }

    @SuppressWarnings("unchecked")
    public <T> T getAs(final String name, final Class<T> cls)
            throws SignatureException {
        final OtpErlangObject v = get(name);
        return (T) TypeConverter.erlang2java(v, cls);
    }

    public void put(final String name, final OtpErlangObject value) {
        bindings.put(name, value);
    }

    public Map<String, OtpErlangObject> getAll() {
        return Collections.unmodifiableMap(bindings);
    }

    @Override
    public String toString() {
        return bindings.toString();
    }

}
