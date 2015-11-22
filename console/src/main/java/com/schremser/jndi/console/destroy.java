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
import java.util.Vector;

public class destroy implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        // Check to see if we have the name we need
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No name specified");

        String name = (String)v.firstElement();

        try {
            c.destroySubcontext(name);
            System.out.println("Destroyed " + name);
        }
        catch (NameNotFoundException nnfe) {
            throw new CommandException(nnfe, "Couldn't find " + name);
        }
        catch (NotContextException nce) {
            throw new CommandException(nce,
                    name + " is not a Context and couldn't be destroyed");
        }
        catch (ContextNotEmptyException cnee) {
            throw new CommandException(cnee,
                    name + " is not empty and couldn't be destroyed");
        }
        catch (NamingException ne) {
            throw new CommandException(ne, name + " couldn't be destroyed");
        }
    }

    public String help() { return ("Usage: destroy [name]"); }
}