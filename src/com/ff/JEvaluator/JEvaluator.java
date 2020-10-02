/*
 * Created on Nov 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ff.JEvaluator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author fiorucci
 * 
 */
public class JEvaluator
{
    protected static Log log = LogFactory.getLog(JEvaluator.class);

    private HashMap<String, String> evaluatorNames = new HashMap<String, String>();
    private HashMap<String, JEvaluatorNodeBaseClass> evaluatorExprs = new HashMap<String, JEvaluatorNodeBaseClass>();

    private HashMap<String, BaseFunctionInterface> evaluatorFuncs = new HashMap<String, BaseFunctionInterface>();

    public JEvaluator()
    {
    }

    public boolean init(String configFile)
    {
        // log4j's logger initialization
        try
        {
            PropertyConfigurator.configure(configFile);
        } catch (Exception e)
        {
            log.error("Error opening " + configFile);

            return false;
        }

        log.info("Starting JEvaluator v0.6");

        return true;
    }

    public JEvaluator(String varName, String expr)
    {
        evaluatorNames.put(varName, varName);
        evaluatorExprs.put(varName, parse(expr));
    }

    /**
     * Creates an evaluator from an expression such as "varName=expr"
     * 
     * @param varEqualsExpr
     */
    public JEvaluator(String varEqualsExpr)
    {
        String varName = varEqualsExpr.split("=")[0];
        String expr = varEqualsExpr.split("=")[1];

        evaluatorNames.put(varName, varName);
        evaluatorExprs.put(varName, parse(expr));
    }

    private void recursiveDumper(JEvaluatorNodeBaseClass n)
    {
        if (n == null)
            return;

        // Dumps current node content
        n.dump();

        if (n.getSymbolType() != JEvaluatorNodeTypes.EVAL_FUNCTION)
        {
            // Current node is an expression, dumps left and right operand
            // subtrees
            recursiveDumper(((JEvaluatorNodeExpr) n).getLeft());
            recursiveDumper(((JEvaluatorNodeExpr) n).getRight());
        } else
        {
            // Current node is a function, dumps all parameter trees
            for (int i = 0; i < ((JEvaluatorNodeFunc) n)
                    .getNumberOfParameters(); i++)
                recursiveDumper(((JEvaluatorNodeFunc) n).getParameter(i));
        }
    }

    public void dump(String varName)
    {
        recursiveDumper((JEvaluatorNodeBaseClass) evaluatorExprs.get(varName));
    }

    private void exprCharDump(JEvaluatorExprChar[] exprChars,
            int startPosition, int endPosition)
    {
        String expr = "", nestingLevels = "", types = "";

        for (int i = startPosition; i <= endPosition; i++)
        {
            expr += exprChars[i].getSymbol();
            nestingLevels += exprChars[i].getNestingLevel();
            types += exprChars[i].getSymbolType();
        }
    }

    /**
     * Recursively parses the specified mathematical expression
     * 
     * @param exprChars
     *            An evaluatorExprChar array
     * @param startPosition
     *            The leftmost evaluatorExprChar array element to consider
     * @param endPosition
     *            The number of items in the array
     * @return an evaluatorNode
     */
    private JEvaluatorNodeBaseClass recursiveParser(
            JEvaluatorExprChar[] exprChars, int startPosition, int endPosition)
    {
        exprCharDump(exprChars, startPosition, endPosition);

        if (startPosition > endPosition)
            return null;

        int rootNodeIndex = startPosition;
        int i = startPosition;

        // Example:
        // (2+c)*(a-4) <-- Expression
        // 11111011111 <-- Nesting levels
        // 63147264137 <-- EvaluatorNodeTypes
        // -----------
        // 01234567890 <-- Expression character index

        // Looks for the expression item having the highest priority, at the
        // deepest nesting level
        // In the example above, rootNodeIndexes are, in sequence, at Expression
        // character indexes:
        // 5 (*), 2 (+), 8 (-)
        // [*]
        // |
        // [+]-----[-]
        // |.......|
        // [2]-[c] [a]-[4]
        //
        while (i <= endPosition)
        {
            // Finds the less deep/external nesting level
            if (exprChars[i].getNestingLevel() < exprChars[rootNodeIndex]
                    .getNestingLevel())
                rootNodeIndex = i;

            // If exprChar belongs to current less deep/external nesting
            // level...
            if (exprChars[i].getNestingLevel() == exprChars[rootNodeIndex]
                    .getNestingLevel())
            {
                // ...seeks for an operator to take as rootNode, +- have lower
                // priority so they must end up on top of the binary tree
                if (exprChars[i].getSymbolType() == JEvaluatorNodeTypes.EVAL_OPERATOR_PLUSMINUS)
                    rootNodeIndex = i;

                // An */^ operator at deepest nesting level can only become
                // rootNode if the previously considered root node was also */^
                // (ie. in (2+a*7)^(b-3), "+" has lower priority over "*",
                // while in (2*a*7)^(b-3), we consider the second "*" as the
                // most prioritary one
                if (exprChars[i].getSymbolType() == JEvaluatorNodeTypes.EVAL_OPERATOR_MULTIPLYDIVIDEPOWER
                        && exprChars[rootNodeIndex].getSymbolType() != JEvaluatorNodeTypes.EVAL_OPERATOR_PLUSMINUS)
                    rootNodeIndex = i;
            }

            i++;
        }

        // log.debug("rootNodeIndex at " + rootNodeIndex);

        // Builds the Evaluator's expression tree
        JEvaluatorNodeBaseClass node = null;

        if (exprChars[rootNodeIndex].getSymbolType() == JEvaluatorNodeTypes.EVAL_CONSTANT
                || exprChars[rootNodeIndex].getSymbolType() == JEvaluatorNodeTypes.EVAL_VARIABLE)
        {
            // If rootNode points at a constant or variable symbol, populates
            // the node

            node = new JEvaluatorNodeExpr();

            String s = "";

            node.setSymbolType(exprChars[rootNodeIndex].getSymbolType());
            for (int j = startPosition; j <= endPosition; j++)
            {
                s += exprChars[j].getSymbol();
            }

            node.setSymbol(s);
        } else if (exprChars[rootNodeIndex].getSymbolType() == JEvaluatorNodeTypes.EVAL_FUNCTION)
        {
            // rootNode points to a function call f(p1,p2,p3,...,pn), populates
            // the node

            node = new JEvaluatorNodeFunc();

            // Retrieves the function's name: loops until the exprChar type is
            // EVAL_FUNCTION (5) and while the nesting level is the same of the
            // first letter in the function's name
            // Example:
            // sum(avg(1,2,6),2,avg(10,20),3)
            // Start...... [0] Stop [23]
            // Expression. [sumavg1,2,6,2,avg10,20,3]
            // Weights.... [000111222221111112222211]
            // Types...... [555555383838385553383383]
            String s = "";
            int j = startPosition;

            while (j <= endPosition
                    && exprChars[j].getSymbolType() == JEvaluatorNodeTypes.EVAL_FUNCTION
                    && exprChars[j].getNestingLevel() == exprChars[startPosition]
                            .getNestingLevel())
                s += exprChars[j++].getSymbol();

            node.setSymbol(s);
            node.setSymbolType(exprChars[rootNodeIndex].getSymbolType());

            /*
             * log.debug("Function [" + node.getSymbol() + "] at [" +
             * rootNodeIndex + "-" + j + "] start-stop[" + startPosition + "-" +
             * endPosition + "]");
             */

            // Counts function's parameters
            int paramStart = j, paramEnd = 0, paramCounter = 0;
            while (paramStart <= endPosition && paramEnd <= endPosition)
            {
                // Finds the length of the current parameter string, a
                // function's parameters are delimited by comma. Loops until it
                // find the next comma character belonging to the same or to a
                // deeper nesting level as the first character of the parameter
                paramEnd = paramStart;

                while (paramEnd <= endPosition
                        && (exprChars[paramEnd].getSymbolType() != JEvaluatorNodeTypes.EVAL_FUNCTION_COMMA || exprChars[paramEnd]
                                .getNestingLevel() > exprChars[paramStart]
                                .getNestingLevel()))
                    paramEnd++;

                paramCounter++;

                // Adds parameter to the list of those belonging to the function
                ((JEvaluatorNodeFunc) node).setParameter(paramCounter,
                        recursiveParser(exprChars, paramStart, paramEnd - 1));

                paramStart = paramEnd + 1;
            }
        } else if (exprChars[rootNodeIndex].getSymbolType() == JEvaluatorNodeTypes.EVAL_LABEL)
        {
            node = new JEvaluatorNodeLabel();

            node.setSymbolType(exprChars[rootNodeIndex].getSymbolType());

            String s = "";
            int j = startPosition;

            while (j <= endPosition
                    && exprChars[j].getSymbolType() == JEvaluatorNodeTypes.EVAL_LABEL)
                s += exprChars[j++].getSymbol();

            node.setSymbol(s);
        } else
        {
            // rootNode points to an operator, populates the node
            // node object type is EvaluatorNodeExpr, inherited from
            // EvaluatorNodeBaseClass

            node = new JEvaluatorNodeExpr();

            node.setSymbolType(exprChars[rootNodeIndex].getSymbolType());
            node.setSymbol(exprChars[rootNodeIndex].getSymbol() + "");

            ((JEvaluatorNodeExpr) node).setLeft(recursiveParser(exprChars,
                    startPosition, rootNodeIndex - 1));
            ((JEvaluatorNodeExpr) node).setRight(recursiveParser(exprChars,
                    rootNodeIndex + 1, endPosition));
        }

        return node;
    }

    /**
     * Parses the specified mathematical expression
     * 
     * @param expression
     *            The expression to be parsed
     * @return an EvaluatorNode if parsing was successful, null if expression
     *         has unbalanced parenthesis
     */
    private JEvaluatorNodeBaseClass parse(String expression)
    {
        JEvaluatorExprChar[] exprChars = new JEvaluatorExprChar[expression
                .length()];
        char c;
        int exprCharsCounter = 0, nestingLevel = 0;

        log.debug("Parsing expression [" + expression + "]");

        /*
         * Parses input expression, building the relevant evaluatorExprChar
         * array
         */

        boolean charIsALabel = false;

        for (int i = 0; i < expression.length(); i++)
        {
            c = expression.charAt(i);

            // In case we find a label delimited by "'"
            if (new String(JEvaluatorNodeTypes.EVAL_STRING_LABEL).indexOf(c) != -1)
            {
                charIsALabel = !charIsALabel;

                // if (charIsALabel == true)
                // log.debug("Found label start at " + i);
                // else
                // log.debug("Found label stop at " + i);
            } else
            {
                if (charIsALabel)
                {
                    exprChars[exprCharsCounter++] = new JEvaluatorExprChar(c,
                            JEvaluatorNodeTypes.EVAL_LABEL, nestingLevel);

                    // log.debug("Label: added char at " + i + "/"
                    // + (exprCharsCounter - 1) + ": " + c);
                } else
                {
                    if (new String(
                            JEvaluatorNodeTypes.EVAL_STRING_OPENPARENTHESIS)
                            .indexOf(c) != -1)
                    {
                        // When matching and open parenthesis, checks to see
                        // whether
                        // it
                        // belongs to a function call. If so, steps back on
                        // exprChars[]
                        // setting the function name's symbol type to
                        // EVAL_FUNCTION,
                        // until it reaches the beginning of the expression, or
                        // an
                        // operator. A function call syntax is "funcName(...)"
                        if (i > 0 && exprCharsCounter > 0)
                        {
                            if (exprChars[exprCharsCounter - 1].getSymbolType() == JEvaluatorNodeTypes.EVAL_VARIABLE)
                            {
                                // Found a literal character immediately before
                                // the
                                // open
                                // parenthesis

                                int j = exprCharsCounter;
                                while (--j >= 0)
                                {
                                    // log.debug("exprChars[" + j + "]="
                                    // + exprChars[j].getSymbolType());

                                    // Marks the function name as EVAL_FUNCTION
                                    // in
                                    // exprChars[]
                                    if (exprChars[j].getSymbolType() == JEvaluatorNodeTypes.EVAL_VARIABLE)
                                    {
                                        exprChars[j]
                                                .setSymbolType(JEvaluatorNodeTypes.EVAL_FUNCTION);
                                    } else
                                        j = -1;

                                }
                            }
                        }
                    }

                    if (new String(
                            JEvaluatorNodeTypes.EVAL_STRING_OPENPARENTHESIS)
                            .indexOf(c) != -1)
                        nestingLevel++;
                    if (new String(
                            JEvaluatorNodeTypes.EVAL_STRING_CLOSEDPARENTHESIS)
                            .indexOf(c) != -1)
                        nestingLevel--;
                    if (new String(
                            JEvaluatorNodeTypes.EVAL_STRING_OPERATOR_PLUSMINUS)
                            .indexOf(c) != -1)
                        exprChars[exprCharsCounter++] = new JEvaluatorExprChar(
                                c, JEvaluatorNodeTypes.EVAL_OPERATOR_PLUSMINUS,
                                nestingLevel);
                    if (new String(
                            JEvaluatorNodeTypes.EVAL_STRING_OPERATOR_MULTIPLYDIVIDEPOWER)
                            .indexOf(c) != -1)
                        exprChars[exprCharsCounter++] = new JEvaluatorExprChar(
                                c,
                                JEvaluatorNodeTypes.EVAL_OPERATOR_MULTIPLYDIVIDEPOWER,
                                nestingLevel);
                    if (new String(JEvaluatorNodeTypes.EVAL_STRING_CONSTANT)
                            .indexOf(c) != -1)
                        exprChars[exprCharsCounter++] = new JEvaluatorExprChar(
                                c, JEvaluatorNodeTypes.EVAL_CONSTANT,
                                nestingLevel);
                    if (new String(JEvaluatorNodeTypes.EVAL_STRING_VARIABLE)
                            .indexOf(c) != -1)
                        exprChars[exprCharsCounter++] = new JEvaluatorExprChar(
                                c, JEvaluatorNodeTypes.EVAL_VARIABLE,
                                nestingLevel);
                    if (new String(JEvaluatorNodeTypes.EVAL_STRING_COMMA)
                            .indexOf(c) != -1)
                        exprChars[exprCharsCounter++] = new JEvaluatorExprChar(
                                c, JEvaluatorNodeTypes.EVAL_FUNCTION_COMMA,
                                nestingLevel);
                }
            }
        }

        if (nestingLevel == 0)
            return recursiveParser(exprChars, 0, exprCharsCounter - 1);

        return null;
    }

    /**
     * Recursively traverses this evaluator instance's tree, rebuilding the
     * mathematical expression string for later printout
     * 
     * @param n
     *            the root node
     * @return the expression string for this subtree
     */
    private String getExpressionRecursive(JEvaluatorNodeBaseClass n)
    {
        if (n == null)
            return "";

        if (n.getSymbolType() != JEvaluatorNodeTypes.EVAL_FUNCTION)
        {
            // Current node is an expression, rebuilds left and right operands
            if (((JEvaluatorNodeExpr) n).getLeft() == null
                    && ((JEvaluatorNodeExpr) n).getRight() == null)
                return n.getSymbol();

            return "("
                    + getExpressionRecursive(((JEvaluatorNodeExpr) n).getLeft())
                    + ")"
                    + n.getSymbol()
                    + "("
                    + getExpressionRecursive(((JEvaluatorNodeExpr) n)
                            .getRight()) + ")";
        } else
        {
            // Current node is a function, rebuilds parameters subtrees
            log.debug("*** CODE TO BE WRITTEN ***");
        }

        return null;
    }

    /**
     * Recursively computes a subtree starting from the given rootNode
     * 
     * @param n
     *            the rootNode
     * @return the subtree result
     */
    protected String compute(JEvaluatorNodeBaseClass n) throws Exception
    {
        if (n == null)
            return "0";

        switch (n.getSymbolType())
        {
            case JEvaluatorNodeTypes.EVAL_OPERATOR_MULTIPLYDIVIDEPOWER:
            case JEvaluatorNodeTypes.EVAL_OPERATOR_PLUSMINUS:
                double leftOperand = Double
                        .parseDouble(compute(((JEvaluatorNodeExpr) n).getLeft()));
                double rightOperand = Double
                        .parseDouble(compute(((JEvaluatorNodeExpr) n)
                                .getRight()));

                switch (n.getSymbol().charAt(0))
                {
                    case '+':
                        return Double.toString(leftOperand + rightOperand);
                    case '-':
                        return Double.toString(leftOperand - rightOperand);
                    case '*':
                        return Double.toString(leftOperand * rightOperand);
                    case '/':
                        return Double.toString(leftOperand / rightOperand);
                    case '^':
                        return Double.toString(java.lang.Math.pow(leftOperand,
                                rightOperand));
                    case '>':
                        return (leftOperand > rightOperand ? "1" : "0");
                    case '<':
                        return (leftOperand < rightOperand ? "1" : "0");
                    case '?':
                        return (leftOperand == rightOperand ? "1" : "0");
                    case '&':
                        return Double.toString(Double.valueOf(leftOperand)
                                .intValue()
                                & Double.valueOf(rightOperand).intValue());
                    case '|':
                        return Double.toString(Double.valueOf(leftOperand)
                                .intValue()
                                | Double.valueOf(rightOperand).intValue());
                    case '!':
                        return Double.toString(Double.valueOf(leftOperand)
                                .intValue()
                                ^ Double.valueOf(rightOperand).intValue());
                    default:
                        break;
                }
                break;

            case JEvaluatorNodeTypes.EVAL_VARIABLE:
                return getVar(n.getSymbol());

            case JEvaluatorNodeTypes.EVAL_CONSTANT:
                return n.getSymbol();

            case JEvaluatorNodeTypes.EVAL_FUNCTION:
                // Node is a function, computes it over its parameters
                return computeFunction(n);

            case JEvaluatorNodeTypes.EVAL_LABEL:
                // Node is a label, returns it
                return n.getSymbol();

            default:
                break;
        }

        return "0";
    }

    private String computeFunction(JEvaluatorNodeBaseClass n) throws Exception
    {
        // The target function to be computed
        BaseFunctionInterface bfi = null;

        if ((bfi = initFunctionClass(n.getSymbol())) == null)
            return "0";

        // Build parameter list
        List<String> funcParams = buildParamList((JEvaluatorNodeFunc) n);

        // computes selected function

        if (bfi.checkSyntax(funcParams) == JEvaluatorErrcodes.PARSE_INVALID_NUMBER_OF_ARGUMENTS)
            throw new NoSuchFieldException(
                    "Invalid number of arguments for function '"
                            + n.getSymbol() + "'");
        else if (bfi.checkSyntax(funcParams) == JEvaluatorErrcodes.PARSE_UNBALANCED_EXPR)
            throw new ParseException("Unbalanced expression for function '"
                    + n.getSymbol() + "'", 0);

        // Evaluates f(...)
        String result = bfi.compute(funcParams);
        log.debug("F(x)=[" + result + "]");

        return result;
    }

    /**
     * Instantiates a BaseFunctionInterface for later computation, based on the
     * function's name
     * 
     * @param fName
     *            the function's name (sum, avg, ...)
     * @return a BaseFunctionInterface instance, for later bfi.compute() call
     */
    protected BaseFunctionInterface initFunctionClass(String fName)
            throws JEvaluatorException
    {
        if (isFunctionRegistered(fName) == true)
            return (BaseFunctionInterface) evaluatorFuncs.get(fName);
        else
            throw new JEvaluatorException("Unregistered function " + fName);
    }

    /**
     * Computes function parameter values and builds list of "double" values to
     * be later passed to the compute() method of the selected function
     * 
     * @param f
     *            the evaluator node function
     * @return the list of Double parameters to be passed to the function
     */
    protected List<String> buildParamList(JEvaluatorNodeFunc f)
            throws Exception
    {
        // Parameters to be passed to the appropriate Function class
        List<String> funcParams = Collections
                .synchronizedList(new ArrayList<String>());

        // Builds the list of parameters ("double" numbers) to be passed to the
        // "compute" method of the chosen function
        log.debug("-- Adding funcParms[]");
        for (int i = 0; i < f.getNumberOfParameters(); i++)
        {
            funcParams.add(i, new String(compute(f.getParameter(i))));
            log.debug("Adding funcParm[" + i + "]=[" + funcParams.get(i) + "]");
        }
        log.debug("---------------------");

        return funcParams;
    }

    /**
     * Evaluates a text that includes evaluable expressions in the format
     * ${expr}
     * 
     * @param text
     *            the text to be evaluated, all inline expressions are expanded
     *            with their values
     * @param varName
     *            the variable whose value has to be calculated and replaced in
     *            the parsed text
     * @return the evaluated and expanded text
     */
    public String evaluateText(String text, String varName) throws Exception
    {
        String parsedText = "";

        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == '$' && i < text.length() - 1
                    && text.charAt(i + 1) == '{')
            {
                // Found an expression that needs to be parsed/evaluated
                String expression = "";

                // Skips the leading '$' character
                i++;

                while (text.charAt(++i) != '}' && i < text.length())
                {
                    expression += text.charAt(i);
                }

                setExpr(expression);

                // Evaluates all expressions in the {...} block
                // and expands the {...} block with the content of the specified
                // variable
                parsedText += getVar(varName);
            } else
                parsedText += text.charAt(i);
        }

        return parsedText;
    }

    /**
     * Sets a variable's value for the current evaluator instance, based on an
     * expression in the format "varName=expression"
     * 
     * @param expression
     *            the expression in the format "varName=expression"
     * @return false if varName was newly created, false in case varName's
     *         previous value was overridden
     */
    public boolean setExpr(String expression)
    {
        String[] tokens = expression.split("=");

        // log.debug("Found " + tokens.length + " tokens");

        if (tokens.length == 1)
        {
            // Exception to be thrown
            log.error("Invalid expression [" + expression + "]");
            return false;
        }

        String varName = tokens[0];
        String expr = "";

        // Enqueues all tokens on the right hand side of the first '=' sign
        for (int i = 1; i < tokens.length; i++)
        {
            // log.debug("Enqueueing token[" + tokens[i] + "]");
            expr += tokens[i];

            if (i < tokens.length - 1)
                expr += "=";
        }

        log.debug("setVar[" + varName + "]=[" + expr + "]");

        evaluatorNames.put(varName, varName);

        return evaluatorExprs.put(varName, parse(expr)) == null ? true : false;
    }

    /**
     * Sets a variable's value for the current evaluator instance
     * 
     * @param varName
     *            the variable's name
     * @param expr
     *            the value/expression to set
     * @return false if varName was newly created, false in case varName's
     *         previous value was overridden
     */
    public boolean setVar(String varName, String expr)
    {
        log.debug("setVar[" + varName + "]=[" + expr + "]");

        evaluatorNames.put(varName, varName);

        return evaluatorExprs.put(varName, parse(expr)) == null ? true : false;
    }

    /**
     * Gets a variable's value for the current evaluator instance
     * 
     * @param varName
     *            the variable name
     * @return the variable value
     */
    public String getVar(String varName) throws Exception
    {
        String varValue = compute((JEvaluatorNodeBaseClass) evaluatorExprs
                .get(varName));

        log.debug("getVar[" + varName + "]=[" + varValue + "]");

        return varValue;
    }

    /**
     * Deletes a variable for the current evaluator instance
     * 
     * @param varName
     *            the variable name
     * @return true if deletion was successful, false otherwise (variable not
     *         found)
     */
    public boolean delVar(String varName)
    {
        log.debug("delVar[" + varName + "] found is "
                + evaluatorNames.containsKey(varName));

        if (evaluatorNames.containsKey(varName) == true)
        {

            evaluatorNames.remove(varName);
            evaluatorExprs.remove(varName);
            return true;
        }

        return false;
    }

    /**
     * Checks to see whether varName is defined for the current evaluator
     * instance
     * 
     * @param varName
     *            the variable's name
     * @return true is varName is defined, false otherwise
     */
    public boolean isVarDefined(String varName)
    {
        log.debug("checkVar[" + varName + "] is "
                + evaluatorNames.containsKey(varName));

        return evaluatorNames.containsKey(varName);
    }

    /**
     * Registers a customized function to JEvaluator
     * 
     * @param f
     *            the Function object to register
     */
    public void registerFunction(BaseFunctionInterface f)
    {
        evaluatorFuncs.put(f.getName(), f);
        log.debug("Registered function " + f.getName());
    }

    /**
     * Unregisters a customized function to JEvaluator
     * 
     * @param f
     *            the Function object to unregister
     */
    public void unregisterFunction(BaseFunctionInterface f)
    {
        evaluatorFuncs.remove(f.getName());
        log.debug("Unregistered function " + f.getName());
    }

    /**
     * Unregisters a customized function to JEvaluator
     * 
     * @param functionName
     *            the Function object to unregister
     * @return true if unregistration was successful, false otherwise
     */
    public boolean unregisterFunction(String functionName)
    {
        return evaluatorFuncs.remove(functionName) == null ? false : true;
    }

    /**
     * Checks to see whether a function was registered
     * 
     * @param f
     *            the function object
     * @return true if function was registered, false otherwise
     */
    public boolean isFunctionRegistered(BaseFunctionInterface f)
    {
        return evaluatorFuncs.containsKey(f.getName());
    }

    /**
     * Checks to see whether a function was registered
     * 
     * @param functionName
     *            the function name
     * @return true if function was registered, false otherwise
     */
    public boolean isFunctionRegistered(String functionName)
    {
        return evaluatorFuncs.containsKey(functionName);
    }
}
