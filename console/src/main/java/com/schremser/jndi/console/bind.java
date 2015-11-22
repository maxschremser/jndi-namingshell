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

class bind implements Command {

    public void execute(Context c, Vector v) 
        throws CommandException {

        // check to see if we have all the args we need to bind
        if (v.isEmpty())
            throw new CommandException(new Exception(),
                "No names specified");
        else if (!(v.size() > 1) || v.elementAt(1) == null)
            throw new CommandException(new Exception(),
                "Only one name specified");
        
        String name = (String)v.firstElement();
        String value = (String)v.elementAt(1);
        
        try {
            c.bind(name, new StringRefAddr(name, value));
            System.out.println("Bound " + name + " to " + value);
        }
        catch (NameAlreadyBoundException nabe) {
            throw new CommandException(nabe, 
                "The name " + name + " is already bound");
        }
        catch (OperationNotSupportedException onse) {
            throw new CommandException(onse, 
                "Binding " + value + " to " + name + " is not supported");
        }        
        catch (NamingException ne) {
            throw new CommandException(ne,
                "Couldn't bind " + name + " to "  + value);
        }
    }

    public String help() { return ("Usage: bind [name] [obj]"); }
}
