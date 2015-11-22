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
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.Vector;

class dirattrs implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        String name = "";

        // An empty string is OK for a listattrs operation
        // as it means list attributes of the current context
        if (!(v.isEmpty()))
            name = (String)v.firstElement();

        if (NamingShell.getCurrentContext() == null)
            throw new CommandException(new Exception(), "No current context");

        try {
            // Get the Attributes and then get enumeration of Attribute objects
            Attributes attrs = ((DirContext)c).getAttributes(name);
            NamingEnumeration allAttr = attrs.getAll();
            while (allAttr.hasMore()) {
                Attribute attr = (Attribute)allAttr.next();
                System.out.println("Attribute: " + attr.getID());

                // Note that this can return human-unreadable garbage
                NamingEnumeration values = attr.getAll();
                while (values.hasMore())
                    System.out.println("Value: " + values.next());
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