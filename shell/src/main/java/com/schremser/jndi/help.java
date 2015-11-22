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
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class help implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        String command = "";

        // An empty string is OK for help as it means
        // list all commands.
        if (!(v.isEmpty()))
            command = (String) v.firstElement();

        if (command.equals("")) {
            // list all commands
            URL packagePath = Thread.currentThread().getContextClassLoader().getResource(getClass().getPackage().getName().replace('.','/'));
            System.out.printf("%n%-12s| %s%n", "Command", "Usage");
            System.out.println("----------------------------------");
            for (File clazz : new File(packagePath.getFile()).listFiles()) {
                try {
                    Class commandInterface = Class.forName("com.schremser.jndi.Command");
                    String currentCommand = clazz.getName().replace(".class", "");
                    Class commandClass = Class.forName("com.schremser.jndi." + currentCommand);

                    if (!commandClass.equals(commandInterface) && commandInterface.isAssignableFrom(commandClass)) {
                        Command theCommand = (Command) commandClass.newInstance();
                        System.out.printf("%-12s  %s%n", currentCommand, theCommand.help());
                    }
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("[" + command + "]: command not found");
                } catch (IllegalAccessException iae) {
                    System.out.println("[" + command + "]: illegal acces");
                } catch (InstantiationException ie) {
                    System.out.println("[" + command + "]: command couldn't be instantiated");
                }

            }
        } else {
            try {
                Class commandInterface = Class.forName("com.schremser.jndi.Command");
                Class commandClass = Class.forName("com.schremser.jndi." + command);

                if ((commandInterface.isAssignableFrom(commandClass))) {
                    Command theCommand = (Command) commandClass.newInstance();
                    System.out.println(command + "\t" + theCommand.help());
                }
            } catch (ClassNotFoundException cnfe) {
                System.out.println("[" + command + "]: command not found");
            } catch (IllegalAccessException iae) {
                System.out.println("[" + command + "]: illegal acces");
            } catch (InstantiationException ie) {
                System.out.println("[" + command + "]: command couldn't be instantiated");
            }
        }
    }

    public String help() {
        return ("Usage: help [name]");
    }


}