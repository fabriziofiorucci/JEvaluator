/*
 * Created on Jul 9, 2006
 *
 */
package com.ff.test;

import com.ff.JEvaluator.JEvaluator;
import com.ff.JEvaluator.Functions.And;
import com.ff.JEvaluator.Functions.Avg;
import com.ff.JEvaluator.Functions.If;
import com.ff.JEvaluator.Functions.Case;
import com.ff.JEvaluator.Functions.Or;
import com.ff.JEvaluator.Functions.Strlen;
import com.ff.JEvaluator.Functions.Sum;

public class JEvaluatorTest
{
    public static void main(String args[]) throws Exception
    {
        JEvaluator expr = new JEvaluator();

        if (expr.init("etc/JEvaluator.conf") == false)
        {
            System.out.println("Error initializing log4j");
            return;
        }

        expr.setVar("base", "120");
        expr.setVar("heigth", "45");
        expr.setVar("area", "base*heigth/2");

        expr.setExpr("base2=base+10");

        if (expr.setExpr("area2=") == false)
        {
            System.out.println("Invalid expression!");
        }

        expr.setExpr("area2=(base+base2)*heigth/2");

        System.out.println("Area is: " + expr.getVar("area"));
        System.out.println("Area2 is: " + expr.getVar("area2"));

        expr.registerFunction(new Avg());
        expr.registerFunction(new Sum());
        expr.registerFunction(new If());
        expr.registerFunction(new Case());
        expr.registerFunction(new And());
        expr.registerFunction(new Or());

        System.out.println(expr.isFunctionRegistered("sum"));

        expr.setExpr("average=avg(base,base2,heigth,(base2*2^heigth),5)");
        expr.setExpr("finalSum=sum(10,5)");

        expr.setExpr("ifEvaluation=if(average<5,finalSum,finalSum+1)");

        // if(finalSum<5) -> 0
        // else
        // {
        // if(finalSum<20) -> 50
        // else
        // { 100 }
        // }
        // Range conditions are evaluated left-to-right and the first match
        // stops evaluation
        expr.setExpr("caseEvaluation=case(finalSum<5,0,finalSum<20,50,1,100)");

        System.out.println("Average is: " + expr.getVar("average"));
        System.out.println("Final sum is: " + expr.getVar("finalSum"));
        System.out.println("IF result is: " + expr.getVar("ifEvaluation"));
        System.out.println("Case result is: " + expr.getVar("caseEvaluation"));

        expr.registerFunction(new Strlen());

        expr.setExpr("prova='ciao'");
        expr.setExpr("len=strlen(prova)");

        System.out.println("prova: " + expr.getVar("prova"));
        System.out.println("length: " + expr.getVar("len"));

        System.out
                .println("Evaluated text: "
                        + expr
                                .evaluateText(
                                        "this is the average: ${output=average} and this is our final sum ${output=finalSum}",
                                        "output"));

        expr.setExpr("orTest=or(len,average)");
        System.out
                .println("OR test: "
                        + expr
                                .evaluateText(
                                        "len: ${output=len}, average: ${output=average} -> logical OR: ${output=orTest}",
                                        "output"));

        expr.setVar("average", "0");
        expr.setExpr("andTest=and(len,average)");
        System.out
                .println("AND test: "
                        + expr
                                .evaluateText(
                                        "len: ${output=len}, average: ${output=average} -> logical AND: ${output=andTest}",
                                        "output"));
    }
}
