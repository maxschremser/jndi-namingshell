package com.schremser.jndi.shell

/*
 * Copyright 2003-2011 the original author or authors.
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

import antlr.TokenStreamException
import jline.Terminal
import jline.TerminalFactory
import jline.console.history.FileHistory
import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.tools.shell.*
import org.codehaus.groovy.tools.shell.util.PackageHelper
import org.codehaus.groovy.runtime.StackTraceUtils
import org.codehaus.groovy.tools.shell.util.CurlyCountingGroovyLexer
import org.codehaus.groovy.tools.shell.util.MessageSource
import org.codehaus.groovy.tools.shell.util.Preferences
import org.codehaus.groovy.tools.shell.util.XmlCommandRegistrar
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.fusesource.jansi.AnsiRenderer

import javax.naming.Context

/**
 * An interactive shell for evaluating Groovy code from the command-line (aka. groovysh).
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class Namingsh extends Groovysh {
    static {
        // Install the system adapters
        AnsiConsole.systemInstall()

        // Register jline ansi detector
        Ansi.setDetector(new AnsiDetector())
    }

    static def defaultJndiProps  = ".jndienv";
    static def defaultPrompt = "[no initial context]";
    static Context currentContext, initialContext;
    static def currentName, initialName;

    Namingsh(final ClassLoader classLoader, final Binding binding, final IO io, final Closure registrar) {
        super(io)

        registrar.call(this)
    }


    Namingsh(final ClassLoader classLoader, final Binding binding, final IO io) {
        this(classLoader, binding, io, createDefaultRegistrar(classLoader))
    }

    Namingsh(final Binding binding, final IO io) {
        this(Thread.currentThread().contextClassLoader, binding, io)
    }

    Namingsh(final IO io) {
        this(new Binding(), io)
    }

    Namingsh() {
        this(new IO())
    }

    void setCurrentName(name) {
        currentName = name
        setDefaultPrompt(name)
    }

    private static Closure createDefaultRegistrar(final ClassLoader classLoader) {
        return {Shell shell ->
            def r = new XmlCommandRegistrar(shell, classLoader)
            r.register(getClass().getResource('commands.xml'))
        }
    }

    //
    // ACCESSORS
    //
    private AnsiRenderer prompt = new AnsiRenderer()


    //
    // Execution
    //

    @Override
    String renderPrompt() {
        return prompt.render(getDefaultPrompt())
    }

    final Closure defaultErrorHook = { Throwable cause ->
        assert cause != null

        io.err.println("@|bold,red ERROR|@ ${cause.getClass().name}:")
        io.err.println("@|bold,red ${cause.message}|@")

        super.maybeRecordError(cause)

        if (log.debug) {
            // If we have debug enabled then skip the fancy bits below
            log.debug(cause)
        }
        else {
            boolean sanitize = Preferences.sanitizeStackTrace

            // Sanitize the stack trace unless we are in verbose mode, or the user has request otherwise
            if (!io.verbose && sanitize) {
                cause = StackTraceUtils.deepSanitize(cause);
            }

            def trace = cause.stackTrace

            def buff = new StringBuffer()

            boolean doBreak = false;

            for (e in trace) {
                // Stop the trace once we find the root of the evaluated script
                if (e.className == Interpreter.SCRIPT_FILENAME && e.methodName == 'run') {
                    if (io.verbosity != IO.Verbosity.DEBUG && io.verbosity != IO.Verbosity.VERBOSE) {
                        break
                    }
                    doBreak = true
                }

                buff << "        @|bold at|@ ${e.className}.${e.methodName} (@|bold "

                buff << (e.nativeMethod ? 'Native Method' :
                        (e.fileName != null && e.lineNumber != -1 ? "${e.fileName}:${e.lineNumber}" :
                                (e.fileName != null ? e.fileName : 'Unknown Source')))

                buff << '|@)'

                io.err.println(buff)

                buff.setLength(0) // Reset the buffer
                if (doBreak) {
                    io.err.println('        @|bold ...|@')
                    break
                }
            }
        }
    }

    Closure errorHook = defaultErrorHook

    private void displayError(final Throwable cause) {
        if (errorHook == null) {
            throw new IllegalStateException("Error hook is not set")
        }
        if (cause instanceof MissingPropertyException) {
            if (cause.type && cause.type.canonicalName == Interpreter.SCRIPT_FILENAME) {
                io.err.println("@|bold,red Unknown property|@: " + cause.property)
                return
            }
        }

        errorHook.call(cause)
    }

}
