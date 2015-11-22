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
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import java.util.Vector;

public class create implements Command {

    public void execute(Context c, Vector v) throws CommandException {

        // Check to see if we have the name we need to create a context
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No name specified");

        String name = (String)v.firstElement();
        try {
            c.createSubcontext(name);
            System.out.println("Created " + name);
        }
        catch (NoPermissionException npe) {
            throw new CommandException(npe,
                    "You don't have permission to create " + name + " at this context");
        }
        catch (NamingException ne) {
            throw new CommandException(ne, "Couldn't create " + name);
        }
    }

    public String help() {  return ("Usage: create [name]"); }
}