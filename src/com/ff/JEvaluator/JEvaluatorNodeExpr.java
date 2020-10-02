/*
 * Created on Nov 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ff.JEvaluator;

/**
 * @author fiorucci
 * 
 * Implements the node for the binary tree of the evaluator object, to handle
 * and evaluate mathematical expressions
 */
public class JEvaluatorNodeExpr extends JEvaluatorNodeBaseClass
{
    private JEvaluatorNodeBaseClass left = null;
    private JEvaluatorNodeBaseClass right = null;

    /**
     * @return Returns the left.
     */
    public JEvaluatorNodeBaseClass getLeft()
    {
        return left;
    }

    /**
     * @param left
     *            The left to set.
     */
    public void setLeft(JEvaluatorNodeBaseClass left)
    {
        this.left = left;
    }

    /**
     * @return Returns the right.
     */
    public JEvaluatorNodeBaseClass getRight()
    {
        return right;
    }

    /**
     * @param right
     *            The right to set.
     */
    public void setRight(JEvaluatorNodeBaseClass right)
    {
        this.right = right;
    }
}
