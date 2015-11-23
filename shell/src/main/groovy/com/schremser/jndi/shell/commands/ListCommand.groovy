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
import org.codehaus.groovy.tools.shell.completion.FileNameCompleter

import javax.naming.Context
import javax.naming.NameClassPair
import javax.naming.NamingEnumeration
import javax.naming.NamingException

/**
 * The 'ls' command.
 */
class ListCommand
        extends CommandSupport
{

    ListCommand(final Groovysh shell) {
        super(shell, 'list', 'ls')
    }


    protected List createCompleters() {
        return [ new FileNameCompleter() ]
    }

    Object execute(final List<String> args) {
        assert args != null

        if (((Namingsh) shell).getCurrentContext() == null)
            fail(messages.format('error.no_current_context', args.join(' ')))

        ls(args[0]?:"")
    }

    private void ls(final String name) {
        // Check for current context; throw an exception if there isn't one
        if (((Namingsh) shell).getCurrentContext() == null)
            fail(messages.format('error.no_current_context', name))

        // Call list() and then loop through the results, printing the names
        // and class names of the children
        try {
            NamingEnumeration enumeration = ((Namingsh) shell).getCurrentContext().list(name);
            while (enumeration.hasMore()) {
                NameClassPair ncPair = (NameClassPair)enumeration.next();
                println(messages.format("info.list", ncPair.getName(), ncPair.getClassName()))
            }
        }
        catch (NamingException e) {
            fail(messages.format('error.list', name))
        }
    }
}
