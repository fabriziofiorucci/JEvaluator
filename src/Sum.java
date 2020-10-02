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
public class Sum implements BaseFunctionInterface
{
    /**
     * Computes the sum of all input parameters: sum(p1,p2,...,pn)
     */
    public String compute(List<String> p)
    {
        double total = 0;

        for (int i = 0; i < p.size(); i++)
        {
            log.debug("Sum parameter [" + i + "] = [" + p.get(i) + "]");

            total = total + Double.parseDouble(p.get(i));
        }

        return Double.toString(total);
    }

    public int checkSyntax(List<String> p)
    {
        if (p.size() == 0)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }
    
    public String getName()
    {
        return "sum";
    }
}
