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
public class Case implements BaseFunctionInterface
{
    /**
     * Evaluates a multicondition, assigning different answers based on the
     * evaluation outcome
     * 
     * range(cond1,out1,cond2,out2,...)
     * 
     * if(cond1) out1 else if(cond2) out2 else ... else "NaN"
     * 
     */
    public String compute(List p)
    {
        log.debug("RANGE SIZE: " + p.size());
        for (int i = 0; i < p.size(); i+=2)
        {
            String condition = p.get(i).toString();

            log.debug("RANGE " + i + ": " + condition+" ("+p.get(i)+" / "+p.get(i+1)+")");

            if (new Double(condition).doubleValue() != 0)
                return (String)p.get(i + 1);
        }

        return Double.toString(Double.NaN);
    }

    public int checkSyntax(List p)
    {
        if (p.size() % 2 != 0)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }

    public String getName()
    {
        return "case";
    }
}
