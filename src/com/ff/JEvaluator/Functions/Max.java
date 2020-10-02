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
public class Max implements BaseFunctionInterface
{
    /**
     * Returns the parameter having the maximum value: min(p1,p2,...,pn)
     */
    public String compute(List p)
    {
        double max = Double.parseDouble((String)p.get(0));
        int i = 0;

        while (i < p.size())
        {
            if (Double.parseDouble((String)p.get(i)) > max)
                max = Double.parseDouble((String)p.get(i));
            i++;
        }

        return Double.toString(max);
    }

    public int checkSyntax(List p)
    {
        if (p.size() == 0)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }

    public String getName()
    {
        return "max";
    }
}
