/*
 * Created on Jan 16th, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ff.JEvaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author fiorucci
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JEvaluatorNodeBaseClass
{
    protected static Log log = LogFactory.getLog(JEvaluatorNodeBaseClass.class);

    protected String symbol = null;
    protected int symbolType = 0;

    public void dump()
    {
        log.debug("Type [" + getSymbolType() + "] = [" + getSymbol() + "]");
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public int getSymbolType()
    {
        return symbolType;
    }

    public void setSymbolType(int symbolType)
    {
        this.symbolType = symbolType;
    }
}
