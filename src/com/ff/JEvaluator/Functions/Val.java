/*
 * Created on Jan 16, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ff.JEvaluator.Functions;

import java.util.List;

import com.ff.JEvaluator.*;

/**
 * @author fiorucci
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Val implements BaseFunctionInterface
{
    /**
     * Returns the argument value val(p) Useful for functions that return a
     * single value instead of a list of values
     */
    public String compute(List p)
    {
        log.debug("Val[" + p.get(0) + "]");

        return (String)p.get(0);
    }

    public int checkSyntax(List p)
    {
        if (p.size() != 1)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }

    public String getName()
    {
        return "val";
    }
}
