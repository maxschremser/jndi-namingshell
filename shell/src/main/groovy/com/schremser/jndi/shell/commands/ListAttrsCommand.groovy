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
import javax.naming.NameClassPair
import javax.naming.NamingEnumeration
import javax.naming.NamingException

/**
 * The 'listattrs' command.
 */
class ListAttrsCommand
        extends CommandSupport
{

    ListAttrsCommand(final Groovysh shell) {
        super(shell, 'listattrs', 'la')
    }

    /*
    protected List createCompleters() {
        return [ new FileNameCompleter() ]
    }
    */

    Object execute(final List<String> args) {
        if (((Namingsh) shell).getCurrentContext() == null)
            fail(messages.format('error.no_current_context', args.join(' ')))

        listattrs(args[0]?:"")
    }

    private void listattrs(final String name) {
        // An empty string is OK for a listattrs operation
        // as it means list attributes of the current context

        if (((Namingsh) shell).getCurrentContext() == null)
            fail(messages.format('error.no_current_context', name))

        try {
            NamingEnumeration enumeration = ((Namingsh) shell).getCurrentContext().list(name);
            while (enumeration.hasMore()) {
                NameClassPair ncPair = (NameClassPair)enumeration.next();
                Object obj = ((Namingsh) shell).getCurrentContext().lookup(ncPair.getName());
                println(messages.format("info.listattrs", ncPair.getName(), obj))
            }
        }
        catch (NamingException e) {
            fail(messages.format('error.no_attributes', name))
        }
    }
}
