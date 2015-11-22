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

class unbind implements Command {
    
    public void execute(Context c, Vector v) 
        throws CommandException {
            
        // check to see if we have all the args we need to rebind
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No names specified");
                
        String name = (String)v.firstElement();

        try {
            c.unbind(name);
            System.out.println("Unbound the object " + name);
        }
        catch (NamingException ne) {
            throw new CommandException (ne, "Couldn't unbind " + name);
        }
    }

    public String help() { return ("Usage: unbind [name]"); }
}
