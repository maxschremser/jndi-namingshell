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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

public class initctx implements Command {

    public void execute(Context c, Vector v) {
        String jndiPropsFilename;
        // If no properties file is specified, use the default file;
        // otherwise use the specified file
        if (v.isEmpty())
            jndiPropsFilename = NamingShell.getDefaultPropsFilename();
        else
            jndiPropsFilename = (String)v.firstElement();

        try {
            Properties props = new Properties();
            File jndiProps = new File(jndiPropsFilename);
            props.load(new FileInputStream(jndiProps));

            NamingShell.setInitialContext(new InitialContext(props));
            NamingShell.setInitialName("/");
            NamingShell.setCurrentContext(NamingShell.getInitialContext());
            NamingShell.setCurrentName(NamingShell.getInitialName());
            System.out.print("Created initial context using ");
            System.out.println(jndiProps.getAbsolutePath());
        }
        catch (NamingException ne) {
            System.out.println("Couldn't create the initial context");
        }
        catch (FileNotFoundException fnfe) {
            System.out.print("Couldn't find properties file: ");
            System.out.println(jndiPropsFilename);
        }
        catch (IOException ioe) {
            System.out.print("Problem loading the properties file: ");
            System.out.println(jndiPropsFilename);
        }
        catch (Exception e) {
            System.out.println("There was a problem starting the shell");
        }
    }

    public String help() { return ("Usage: initctx [filename]"); }
}