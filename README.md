# jndi-namingshell
A shell for JNDI 

This is a working example for examining naming directoires. 

This application is based on the example from O'Reilly's Java Enterprise in a Nutshell book which can be bought here: http://shop.oreilly.com/product/9780596101428.do
The NamingShell is initialized by a .jndienv file which holds the configuration for the Naming Directory. It consists of 
two entries:
- java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory
- java.naming.provider.url=file:////jndi/openmq

There are many helpfull resources on the web, some are listed here:
- https://www.captechconsulting.com/blogs/a-riff-on-the-reffscontextfactory
- http://docs.oracle.com/javase/jndi/tutorial/basics/naming/list.html
- http://docstore.mik.ua/orelly/java-ent/jenut/ch06_02.htm

# Build the project by running gradle build from the root of the project.
```gradle
gradle build
```

Run the shell by using java
```gradle
gradle installDist
gradle -q console:shell
or
gradle -q shell:shell
```

You will see an output that the NamingShell has started.

Now you can examine your Context.
```bash
initctx console/src/main/resources/.jndiEnv
list
list VERSION
cd VERSION
listattrs
```