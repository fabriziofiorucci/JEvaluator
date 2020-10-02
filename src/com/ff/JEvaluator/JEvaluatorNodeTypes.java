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
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JEvaluatorNodeTypes
{
    public static final int EVAL_NOTYPE = 0;
    public static final int EVAL_OPERATOR_PLUSMINUS = 1;
    public static final int EVAL_OPERATOR_MULTIPLYDIVIDEPOWER = 2;
    public static final int EVAL_CONSTANT = 3;
    public static final int EVAL_VARIABLE = 4;
    public static final int EVAL_FUNCTION = 5;
    public static final int EVAL_OPENPARENTHESIS = 6;
    public static final int EVAL_CLOSEDPARENTHESIS = 7;
    public static final int EVAL_FUNCTION_COMMA = 8;
    public static final int EVAL_LABEL = 9;

    public static final String EVAL_STRING_OPERATOR_PLUSMINUS = "+-><?";
    public static final String EVAL_STRING_OPERATOR_MULTIPLYDIVIDEPOWER = "*/^";
    public static final String EVAL_STRING_CONSTANT = "0123456789.";
    public static final String EVAL_STRING_VARIABLE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
    public static final String EVAL_STRING_OPENPARENTHESIS = "(";
    public static final String EVAL_STRING_CLOSEDPARENTHESIS = ")";
    public static final String EVAL_STRING_COMMA = ",";

    public static final String EVAL_STRING_LABEL = "'";
}
