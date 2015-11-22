package com.schremser.jndi.console;

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

class rename implements Command {

    public void execute(Context c, Vector v) 
        throws CommandException {
            
        // check to see if we have all the args we need to rename
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No names specified");
        else if (v.elementAt(1) == null)
            throw new CommandException(new Exception(), 
                "Only one name specified");

        String oldName = (String)v.firstElement();
        String newName = (String)v.elementAt(1);
        
        try {
            c.rename(oldName, newName);
            System.out.println("Renamed " + oldName + " to " + newName);
        }
        catch (NamingException ne) {
            throw new CommandException(ne, "Couldn't rename " 
                + oldName + " to " + newName);
        }
    }

    public String help() { return ("Usage: rename [old name] [new name]"); }
}
