package com.schremser.jndi;

/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

public class VersionFactory implements ObjectFactory {
    public VersionFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        if (obj instanceof Reference) {
            Reference reference = (Reference) obj;
            Class clazz = Class.forName(reference.getClassName());
            Constructor constructor = clazz.getConstructor((Class[]) null);
            Version version = (Version) constructor.newInstance((Object[]) null);

            version.setMajor(getStringAddress(reference, "major"));
            version.setMinor(getStringAddress(reference, "minor"));
            version.setBuildDate(getStringAddress(reference, "buildDate"));

            return version;
        }
        return obj;
    }

    private String getStringAddress(Reference ref, String addrType) {
        String value = null;

        RefAddr refAddr = ref.get(addrType);
        if (refAddr != null) {
            value = refAddr.getContent().toString();
        }

        return value;
    }
}