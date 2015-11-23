package com.schremser.jndi.shell.commands

import com.schremser.jndi.shell.Namingsh
import jline.console.completer.FileNameCompleter
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Groovysh;
/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NamingException

/**
 * The 'exit' command.
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class InitCtxCommand
        extends CommandSupport
{

    InitCtxCommand(final Groovysh shell) {
        super(shell, 'initctx', 'ic')
    }

    protected List createCompleters() {
        return [ new FileNameCompleter() ]
    }

    Object execute(final List<String> args) {
        assert args != null

        def source = args[0]?:((Namingsh)shell).getDefaultJndiProps()

        if (io.verbose) {
            io.out.printf(messages['info.jndi'], source)
        }

        URL url

        log.debug("Attempting InitialContext from: $url")

        try {
            url = new URL("$source")
        }
        catch (MalformedURLException e) {
            def file = new File("$source")

            if (!file.exists()) {
                fail("File not found: $file") // TODO: i18n
            }

            url = file.toURI().toURL()
        }

        initContext(url)
    }

    void initContext(final URL url) {
        try {
            Properties props = new Properties();
            File jndiProps = new File(url.file);
            props.load(new FileInputStream(jndiProps));
            Context ctx = new InitialContext(props);
            ((Namingsh)shell).setInitialContext(ctx)
            ((Namingsh)shell).setCurrentContext(ctx)
            ((Namingsh)shell).setInitialName("/")
            ((Namingsh)shell).setCurrentName("/")
        }
        catch (NamingException ne) {
            System.out.println("Couldn't create the initial context");
        }
        catch (FileNotFoundException fnfe) {
            System.out.print("Couldn't find properties file: ");
            System.out.println(url);
        }
        catch (IOException ioe) {
            System.out.print("Problem loading the properties file: ");
            System.out.println(url);
        }
        catch (Exception e) {
            System.out.println("There was a problem starting the shell");
        }

    }
}
