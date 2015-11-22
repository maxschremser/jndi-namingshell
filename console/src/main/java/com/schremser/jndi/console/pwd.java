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
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class pwd implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        String command = "";

        // list path working directory
        URL packagePath = Thread.currentThread().getContextClassLoader().getResource(".");
        try {
            System.out.println(new File(packagePath.toURI()).getAbsolutePath());
        } catch (Exception e) {
            System.out.println(packagePath.toString());
        }
    }

    public String help() {
        return ("Usage: pwd");
    }


}