package com.schremser.jndi.console;

/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class Lookup {        
  public static void main(String[] args) {        
    String name = "";
    if (args.length != 0) 
      name = args[0];
        
    try {
      // Create a Properties object and set properties appropriately
      Properties props = new Properties();
      props.put(Context.INITIAL_CONTEXT_FACTORY,
          "com.sun.jndi.fscontext.RefFSContextFactory");
      props.put(Context.PROVIDER_URL, "file:///");
            
      // Create the initial context from the properties we just created
      Context initialContext = new InitialContext(props);
            
      // Look up the object
      Object obj = initialContext.lookup(name);
      if (name.equals(""))
        System.out.println("Looked up the initial context");
      else
        System.out.println(name + " is bound to: " + obj);
    }
    catch (NamingException nnfe) {
      System.out.println("Encountered a naming exception");
    }
  }
}
