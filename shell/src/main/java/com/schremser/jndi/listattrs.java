package com.schremser.jndi;
/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.naming.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.Vector;

class listattrs implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        String name = "";

        // An empty string is OK for a listattrs operation
        // as it means list attributes of the current context
        if (!(v.isEmpty()))
            name = (String)v.firstElement();

        if (NamingShell.getCurrentContext() == null)
            throw new CommandException(new Exception(), "No current context");

        try {
            NamingEnumeration enumeration = c.list(name);
            while (enumeration.hasMore()) {
                NameClassPair ncPair = (NameClassPair)enumeration.next();
                System.out.print(ncPair.getName() + " (value ");
                Object obj = c.lookup(ncPair.getName());
                System.out.println(obj + ")");
            }
        }
        catch (NamingException e) {
            throw new CommandException(e, "Couldn't list attributes of " + name);
        }
        catch (ClassCastException cce) {
            throw new CommandException(cce, "Not a directory context");
        }
    }

    public String help() { return ("Usage: listattrs [name]"); }
}