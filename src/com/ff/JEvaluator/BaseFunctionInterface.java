/*
 * Created on Jan 16, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ff.JEvaluator;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author fiorucci
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface BaseFunctionInterface
{
    static Logger log = Logger.getLogger(JEvaluator.class.getName());

    // Returns the value for the given function subtree. Argument is a list of
    // "double" values
    public String compute(List<String> parameters) throws Exception;

    // Checks the function's syntax, number of parameters, etc.
    public int checkSyntax(List<String> parameters);

    // Returns the name of the function
    public String getName();
}
