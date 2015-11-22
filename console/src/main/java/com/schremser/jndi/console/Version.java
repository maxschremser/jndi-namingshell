package com.schremser.jndi.console;

/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.naming.*;

public class Version implements Referenceable {
    String major;
    String minor;
    String buildDate;

    public Version() {
        super();
    }

    public String getVersion() {
        return "v" + getMajor() + "." + getMinor();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    @Override
    public String toString() {
        return "Version{" +
                "major='" + major + '\'' +
                ", minor='" + minor + '\'' +
                ", buildDate='" + buildDate + '\'' +
                '}';
    }

    @Override
    public Reference getReference() throws NamingException {
        Reference reference = new Reference(this.getClass().getName(), VersionFactory.class.getName(), null);
        reference.add(new StringRefAddr("major", getMajor()));
        reference.add(new StringRefAddr("minor", getMinor()));
        reference.add(new StringRefAddr("buildDate", getBuildDate()));

        return reference;
    }
}
