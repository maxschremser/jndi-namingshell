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
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.Vector;

public class list implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        String name = "";

        // An empty string is OK for a list operation as it means
        // list children of the current context.
        if (!(v.isEmpty()))
            name = (String)v.firstElement();

        // Check for current context; throw an exception if there isn't one
        if (NamingShell.getCurrentContext() == null)
            throw new CommandException(new Exception(),
                    "Error: no current context.");

        // Call list() and then loop through the results, printing the names
        // and class names of the children
        try {
            NamingEnumeration enumeration = c.list(name);
            while (enumeration.hasMore()) {
                NameClassPair ncPair = (NameClassPair)enumeration.next();
                System.out.print(ncPair.getName() + " (type ");
                System.out.println(ncPair.getClassName() + ")");
            }
        }
        catch (NamingException e) {
            throw new CommandException(e, "Couldn't list " + name);
        }
    }

    public String help() { return ("Usage: list [name]"); }
}