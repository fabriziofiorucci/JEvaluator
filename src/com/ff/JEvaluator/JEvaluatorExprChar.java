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
 * Implements a bean with relevant information for each single character of an
 * expression
 *  
 */
public class JEvaluatorExprChar
{
    private char symbol = 0;
    private int nestingLevel = 0;
    private int symbolType = JEvaluatorNodeTypes.EVAL_NOTYPE;

    /*
     * Creates an EvaluatorExprChar bean, for a single symbol in the
     * mathematical expression
     * 
     * Symbol: the character from the math expression SymbolType: the character
     * type from EvaluatorNodeTypes NestingLevel: the parenthesis nesting level
     */
    public JEvaluatorExprChar(char Symbol, int SymbolType, int NestingLevel)
    {
        symbol = Symbol;
        symbolType = SymbolType;
        nestingLevel = NestingLevel;
    }

    public JEvaluatorExprChar()
    {
    }

    /**
     * @return Returns the nestingLevel.
     */
    public int getNestingLevel()
    {
        return nestingLevel;
    }

    /**
     * @param nestingLevel
     *            The nestingLevel to set.
     */
    public void setNestingLevel(int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }

    /**
     * @return Returns the symbol.
     */
    public char getSymbol()
    {
        return symbol;
    }

    /**
     * @param symbol
     *            The symbol to set.
     */
    public void setSymbol(char symbol)
    {
        this.symbol = symbol;
    }

    /**
     * @return Returns the symbolType.
     */
    public int getSymbolType()
    {
        return symbolType;
    }

    /**
     * @param symbolType
     *            The symbolType to set.
     */
    public void setSymbolType(int symbolType)
    {
        this.symbolType = symbolType;
    }
}
