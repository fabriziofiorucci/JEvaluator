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
public class If implements BaseFunctionInterface
{
    /**
     * Evaluates a condition:
     * 
     * if(condition,exprIfTrue,exprIfFalse)
     * 
     */
    public String compute(List p)
    {
        String condition = (String)p.get(0);

        if (new Double(condition).doubleValue() != 0)
            return (String)p.get(1);
        else
            return (String)p.get(2);
    }

    public int checkSyntax(List p)
    {
        if (p.size() != 3)
            return JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS;

        return JEvaluatorErrcodes.PARSE_NOERROR;
    }

    public String getName()
    {
        return "if";
    }
}
