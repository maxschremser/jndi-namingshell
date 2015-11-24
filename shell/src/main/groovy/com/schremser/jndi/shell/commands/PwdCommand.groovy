/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

package com.schremser.jndi.shell.commands

import com.schremser.jndi.shell.Namingsh
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Groovysh

import javax.naming.Context
import javax.naming.NamingException

/**
 * The 'pwd' command.
 */
class PwdCommand
        extends CommandSupport
{

    PwdCommand(final Groovysh shell) {
        super(shell, 'pwd', 'p')
    }

    /*
    protected List createCompleters() {
        return [ new FileNameCompleter() ]
    }
    */

    Object execute(final List<String> args) {
        pwd()
    }

    private void pwd(final String name) {
        // list path working directory
        URL packagePath = Thread.currentThread().getContextClassLoader().getResource(".");
        try {
            println(new File(packagePath.toURI()).getAbsolutePath());
        } catch (Exception e) {
            fail(packagePath.toString())
        }
    }
}
