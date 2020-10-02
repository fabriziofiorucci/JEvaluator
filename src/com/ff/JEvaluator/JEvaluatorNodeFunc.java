/*
 * Created on Nov 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ff.JEvaluator;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fiorucci
 * 
 * Implements the node for the binary tree of the evaluator object, to handle
 * and evaluate mathematical expressions
 */
public class JEvaluatorNodeFunc extends JEvaluatorNodeBaseClass
{
    // Function parameters' list
    private List<JEvaluatorNodeBaseClass> params = Collections
            .synchronizedList(new ArrayList<JEvaluatorNodeBaseClass>());

    /**
     * Returns the number of parameters in 'params' ArrayList
     * 
     * @return the number of parameters available in 'params'
     */
    public int getNumberOfParameters()
    {
        return params.size();
    }

    /**
     * Sets the n-th EvaluatorNodeFunc parameter subtree
     * 
     * @param n
     *            the parameters' position in the function call
     * @param p
     *            the parameter subtree's root node
     */
    public void setParameter(int n, JEvaluatorNodeBaseClass p)
    {
        params.add(p);
    }

    /**
     * Returns the n-th EvaluatorNodeFunc parameter subtree
     * 
     * @param n
     *            the number of the parameter to be returned
     * @return the rootnode to the parameter's subtree
     */
    public JEvaluatorNodeBaseClass getParameter(int n)
    {
        return (JEvaluatorNodeBaseClass) params.get(n);
    }
}
