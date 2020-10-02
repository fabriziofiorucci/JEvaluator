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
public class And implements BaseFunctionInterface
{
    /**
     * Computes the logical AND for the given parameters: and(p1,p2)
     */
    public String compute(List p)
    {
        String condition1 = (String) p.get(0);
        String condition2 = (String) p.get(1);

        if (new Double(condition1).doubleValue() != 0
                && new Double(condition2).doubleValue() != 0)
            return "1";
        else
            return "0";
    }

    public int checkSyntax(List p)
    {
        if (p.size() != 2)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }

    public String getName()
    {
        return "and";
    }
}
