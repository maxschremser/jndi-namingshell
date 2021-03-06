package com.schremser.jndi.console;
/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

public class CommandException extends Exception {
    Exception e; // root exception
    CommandException(Exception e, String message) {
        super(message);
        this.e = e;
    }
    public Exception getRootException() {
        return e;
    }
}