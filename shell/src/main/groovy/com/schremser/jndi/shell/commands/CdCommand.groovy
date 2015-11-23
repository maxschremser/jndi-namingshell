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
import jline.console.completer.FileNameCompleter
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Groovysh

import javax.naming.Context
import javax.naming.NamingException

/**
 * The 'cd' command.
 */
class CdCommand
        extends CommandSupport
{

    CdCommand(final Groovysh shell) {
        super(shell, 'cd', 'cd')
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

        cd(args[0])
    }

    private void cd(final String name) {
        assert name

        try {
            if (((name.equals("/")) || (name.equals("\\")))) {
                ((Namingsh) shell).setCurrentContext(((Namingsh) shell).getInitialContext());
                ((Namingsh) shell).setCurrentName(((Namingsh) shell).getInitialName());
                println(messages.format('command.current_context', name))
            }
            else {
                Context c = (Context) (((Namingsh) shell).getCurrentContext()).lookup(name);
                ((Namingsh) shell).setCurrentContext(c);
                ((Namingsh) shell).setCurrentName(name);
                println(messages.format('command.current_context', name))
            }
        }
        catch (NamingException ne) {
            fail(messages.format('error.cannot_change_context', name))
        }
        catch (ClassCastException cce) {
            fail(messages.format('error.not_a_context', name))
        }
    }
}
