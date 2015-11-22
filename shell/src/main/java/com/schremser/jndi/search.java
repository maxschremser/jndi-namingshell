package com.schremser.jndi;
/*
 * This example is from the book "Java Enterprise in a Nutshell".
 * Copyright (c) 1999 by O'Reilly & Associates.
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidSearchFilterException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Vector;

class search implements Command {
    public void execute(Context c, Vector v) throws CommandException {

        if (NamingShell.getCurrentContext() == null)
            throw new CommandException(new Exception(), "No current context");
        else if (v.isEmpty())
            throw new CommandException(new Exception(), "No filter specified");
        String filter = (String)v.firstElement();
        try {
            SearchControls cons = new SearchControls();
            cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration results = ((DirContext)c).search("", filter, cons);
            while (results.hasMore()) {
                SearchResult result = (SearchResult)results.next();
                System.out.println(result.getName());
            }
        }
        catch (InvalidSearchFilterException isfe) {
            throw new CommandException(isfe,
                    "The filter [" + filter + "] is invalid");
        }
        catch (NamingException e) {
            throw new CommandException(e, "The search for " + filter + " failed");
        }
        catch (ClassCastException cce) {
            throw new CommandException(cce, "Not a directory context");
        }
    }

    public String help() { return ("Usage: search filter"); }
}