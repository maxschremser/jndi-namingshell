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
import java.util.Vector;

class cd implements Command {
    public void execute(Context ctx, Vector v) throws CommandException {
        if (NamingShell.getCurrentContext() == null)
            throw new CommandException(new Exception(), "No current context");
        else if (v.isEmpty())
            throw new CommandException(new Exception(), "No name specified");

            // Get args[0] and throw away the other args
        else {
            String name = (String)v.firstElement();
            try {
                if (name.equals("..")) {
                    throw new CommandException(new Exception(),
                            "Contexts don't know about their parents.");
                }
                else if (((name.equals("/")) || (name.equals("\\")))) {
                    NamingShell.setCurrentContext(NamingShell.getInitialContext());
                    NamingShell.setCurrentName(NamingShell.getInitialName());
                    System.out.println("Current context now " + name);
                }
                else {
                    Context c = (Context) (NamingShell.getCurrentContext()).lookup(name);
                    NamingShell.setCurrentContext(c);
                    NamingShell.setCurrentName(name);
                    System.out.println("Current context now " + name);
                }
            }
            catch (NamingException ne) {
                throw new CommandException(ne, "Couldn't change to context " + name);
            }
            catch (ClassCastException cce) {
                throw new CommandException(cce, name + " not a Context");
            }
        }
    }

    public String help() { return ("Usage: cd [name]"); }
}