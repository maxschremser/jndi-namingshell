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
import com.schremser.jndi.shell.reference.Version
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Groovysh

import javax.naming.NameAlreadyBoundException
import javax.naming.NamingException
import javax.naming.NoPermissionException
import javax.naming.OperationNotSupportedException

/**
 * The 'attr' command.
 */
class AttrCommand
        extends CommandSupport
{

    AttrCommand(final Groovysh shell) {
        super(shell, 'attr', 'a')
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

        attr(args)
    }

    private void attr(final String name, final String major, final String minor, final String buildDate) {
        assert name
        assert major
        assert minor
        assert buildDate

        try {
            Version version= new Version();
            version.setMajor(major);
            version.setMinor(minor);
            version.setBuildDate(buildDate);

            ((Namingsh) shell).getCurrentContext().rebind(name, version);
            println(messages.format('info.attr', name, version))
        } catch (NameAlreadyBoundException nabe) {
            fail(messages.format('error.already_bound', name))
        }
        catch (OperationNotSupportedException onse) {
            fail(messages.format('error.not_supported', major, minor, buildDate, name))
        }
        catch (NamingException ne) {
            fail(messages.format('error.bind', name, major, minor, buildDate))
        }
    }
}
