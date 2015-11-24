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
import javax.naming.NoPermissionException

/**
 * The 'create' command.
 */
class CreateCommand
        extends CommandSupport
{

    CreateCommand(final Groovysh shell) {
        super(shell, 'create', 'c')
    }

    /*
    protected List createCompleters() {
        return [ new FileNameCompleter() ]
    }
    */

    Object execute(final List<String> args) {
        assert args != null
        if (args.size() == 0)
            fail(messages.format('error.no_argument'))

        if (((Namingsh) shell).getCurrentContext() == null)
            fail(messages.format('error.no_current_context', args.join(' ')))

        create(args[0])
    }

    private void create(final String name) {
        assert name

        try {
            ((Namingsh) shell).getCurrentContext().createSubcontext(name);
            println(messages.format('info.create', name))
        }
        catch (NoPermissionException npe) {
            fail(messages.format('error.no_permission', name))
        }
        catch (NamingException ne) {
            fail(messages.format('error.create', name))
        }
    }
}
