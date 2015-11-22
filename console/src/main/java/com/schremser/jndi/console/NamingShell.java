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
import java.io.*;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

class NamingShell {

    // Private variables
    private static Hashtable COMMAND_TABLE = new Hashtable();
    private static String  JNDIPROPS_FILENAME  = ".jndienv";
    private static String  PROMPT = "[no initial context]";
    private static String  VERSION = "1.0";
    private static Context CURRENT_CONTEXT, INITIAL_CONTEXT;
    private static String  CURRENT_NAME, INITIAL_NAME;
    private static boolean RUNNING = true;

    // Shell operations
    public static void exit(int status) { System.exit(status); }

    // Accessor methods
    public static Hashtable getCommands() { return COMMAND_TABLE; }
    public static Context getCurrentContext() { return CURRENT_CONTEXT; }
    public static String getCurrentName() { return CURRENT_NAME; }
    public static String getDefaultPropsFilename() { return JNDIPROPS_FILENAME; }
    public static Context getInitialContext() { return INITIAL_CONTEXT; }
    public static String getInitialName() { return INITIAL_NAME; }
    public static String getPrompt() { return PROMPT; }
    public static void setCurrentContext(Context ctx) { CURRENT_CONTEXT = ctx; }
    public static void setInitialContext(Context ctx) { INITIAL_CONTEXT = ctx; }
    public static void setInitialName(String name) { INITIAL_NAME = name; }
    public static void setPrompt(String prompt) { PROMPT = prompt; }
    public static void setCurrentName(String name) {
        CURRENT_NAME = name;
        setPrompt(name);
    }
    public static void setRunning(boolean running) {
        RUNNING = running;
    }

    // Executes a preinstantiated command we are sure is already
    // present in the table
    private static void execute(Command c, Vector v) {
        if (c == null) {
            System.out.println("No command was loaded; cannot execute the command.");
            return;
        }
        try {
            c.execute(CURRENT_CONTEXT, v);
        }
        catch (CommandException ce) {
            System.out.println(ce.getMessage());
        }
    }

    // Another private method that enables us to specify a command
    // by its string name and that loads the command first
    private static void execute(String s, Vector v) {
        execute(loadCommand(s), v);
    }

    // Loads the command specified in commandName; the help command
    // relies on this method
    public static Command loadCommand(String commandName) {
        // The method returns a null command unless some of its
        // internal logic assigns a new reference to it
        Command theCommand = null;

        // First see if the command is already present in the hashtable
        if (COMMAND_TABLE.containsKey(commandName)) {
            theCommand = (Command)COMMAND_TABLE.get(commandName);
            return theCommand;
        }

        try {
            // Here we use a little introspection to see if a class
            // implements Command before we instantiate it
            Class commandInterface = Class.forName("com.schremser.jndi.console.Command");
            Class commandClass = Class.forName("com.schremser.jndi.console." + commandName);

            // Check to see if the class is assignable from Command
            // and if so, put the instance in the command table
            if (!(commandInterface.isAssignableFrom(commandClass)))
                System.out.println("[" + commandName + "]: Not a command");
            else {
                theCommand = (Command)commandClass.newInstance();
                COMMAND_TABLE.put(commandName, theCommand);
                return theCommand;
            }
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("[" + commandName + "]: command not found");
        }
        catch (IllegalAccessException iae) {
            System.out.println("[" + commandName + "]: illegal acces");
        }
        catch (InstantiationException ie) {
            System.out.println("["+commandName+"]: command couldn't be instantiated");
        }
        finally {
            return theCommand;          // theCommand is null if we get here
        }
    }

    // This method reads a line of input, gets the command and arguments
    // within the line of input, and then dynamically loads the command
    // from the current directory of the running shell
    private static void readInput() {
        // Get the input from System.in
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        // Begin reading input
        try {
            while (RUNNING) {
                System.out.print(PROMPT + "% ");

                // Tokenize the line, read each token, and pass the token
                // into a convenient remaining arguments Vector that we
                // pass into the Command
                line = br.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line);
                Vector remainingArgs = new Vector();
                String commandToken = "";
                if (tokenizer.hasMoreTokens()) {
                    commandToken = tokenizer.nextToken();
                    while (tokenizer.hasMoreTokens())
                        remainingArgs.addElement(tokenizer.nextToken());
                }

                // Dynamically load the class for the appropriate command
                // based upon the case-sensitive name of the first token,
                // which is the command token
                if (!(commandToken.equals("")))
                    execute(commandToken, remainingArgs);
            }
        }
        catch (java.io.IOException ioe) {
            System.out.println("Caught an IO exception reading a line of input");
        }
    }
    // Constructor
    NamingShell(String[] args) {
        if (args.length > 0) {
            File jndi = new File(args[0]);
            if (jndi.exists() && jndi.isFile()) {
                try {
                    Properties props = new Properties();
                    props.load(new FileInputStream(jndi));

                    setInitialName("/");
                    setCurrentContext(getInitialContext());
                    setCurrentName(getInitialName());
                    System.out.print("Created initial context using ");
                    System.out.println(jndi.getAbsolutePath());

                } catch (FileNotFoundException fnfe) {
                    System.out.print("Couldn't find properties file: ");
                    System.out.println(jndi);
                } catch (IOException ioe) {
                    System.out.print("Problem loading the properties file: ");
                    System.out.println(jndi);
                }
            }
        }
    }

    // Main method that reads input until the user exits
    public static void main(String[] args) {
        NamingShell shell = new NamingShell(args);
        System.out.println("NamingShell " + VERSION);
        System.out.println("Type help for more information or exit to quit");
        shell.readInput();
        System.out.println("Exiting");
    }
}