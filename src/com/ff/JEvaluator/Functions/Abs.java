/*
 * Created on Jan 18, 2006
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
public class Abs implements BaseFunctionInterface
{
    /**
     * Computes the absolute value of the given parameter: abs(x)
     */
    public String compute(List p)
    {
        return Double
                .toString(java.lang.Math.abs(Double.parseDouble((String)p.get(0))));
    }

    public int checkSyntax(List p)
    {
        if (p.size() != 1)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }

    public String getName()
    {
        return "abs";
    }
}
