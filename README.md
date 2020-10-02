# JEvaluator
JEvaluator is a Java class that implements a fast and flexible mathematical expressions parser and evaluator

### Overview
JEvaluator is a Java class that implements a fast and flexible mathematical expressions parser and evaluator.
It's fully compatible with Java 1.4 and greater.
Complex mathematical expressions can be handled and combined together with virtually no limitations on length and number of used variables.

### JEvaluator main features include:
- No restrictions on the number and length of expressions
- No restrictions on the number of variables that can be used
- Dynamic and automatic creation of variables
- No restrictions on variables names, they can be any alphanumeric string starting with a literal
- Support for single-parameter and multi-parameter functions ( ie. abs(p), avg(p1,p2,...pn) ).
- Functions parameters can be either values or expressions.
- Functions that are currently provided with JEvaluator are:

### Functions
- Abs(p) - Absolute value of p
- And(p1, p2) - Boolean AND (p1 && p2)
- Avg(p1, p2, ..., pn) - Average value of p1, p2, ..., pn
- Case(cond1,result1,cond2,result2,...) - Matches multiple conditions to multiple expressions. If cond1 is true, the result is result1, else if cond2 is true, the result is result2, ... In case no condition is matched, the result is NaN
- If(cond,ifTrue,ifFalse) - if cond is true, result is the expression "ifTrue", "ifFalse" otherwise
- Log(p) - Natural logarithm (base e) of p
- Min(p1, p2, ..., pn) - Minimum value among p1, p2, ..., pn
- Max(p1, p2, ..., pn) - Maximum value among p1, p2, ..., pn
- Or(p1, p2) - Boolean OR (p1 || p2)
- Random(p) - Random value amon 0 and p
- Rounddown(p) - p rounded down to the closest integer
- Roundup(p) - p rounded up to the closest integer
- Sqrt(p) - The square root for p
- Strlen(s) - The length in characters of the string variable
- Sum(p1, p2, ..., pn) - The sum of all parameters
- Val(p) - The value of p

### Support for arithmetical operators:
- \+ Sum
- \- Subtraction
- \* Multiplication
- \/ Division
- \^ Power

### Support for logical operators
logical operators return a boolean value, either 0 or 1
- \< Lesser than - ie. 2<(a*(4-c)) , sqrt(a^2)<7-log(22)
- \> Greater than - ie. avg(4-c,72,sum(1,2,3))>0
- \? Equals to - ie. 2?0 , (a+b)*(c-2/e)?avg(log(c-4),3)

### Support for boolean operators
operands are implicitly converted into integer before performing the boolean operations
- \& Bitwise AND - ie. 2&(a*(4-c)) , sqrt(a^2)&7-log(22)
- \| Bitwise inclusive OR - ie. avg(4-c,72,sum(1,2,3))|32
- \! Bitwise exclusive OR - ie. 3!35 , (a+b)*(c-2/e)!avg(log(c-4),3)

### Support for user-defined functions
User-defined functions are supported and can be easily defined by implementing the BaseFunctionInterface interface. For some quick hint have a look at Sum.java.

### Quickstart
For a quickstart clone this repo have a look at JEvaluatorTest.java and JEvaluator.conf.

Basic steps to start using JEvaluator are:

- Initialization
  - Instantiate JEvaluator
```
JEvaluator expr = new JEvaluator();
```
  - Initialize instance against log4j configuration file
```
if ( expr.init ( "etc/JEvaluator.conf" ) ==false )
{
// LOG4J INIT ERROR, ABORTING
}
```
- Variables definition
  - Using name and value
```
expr.setVar ( "base","120" );
expr.setVar ( "doubleBase","base*2" );
```
  - Using name and value as an expression
```
expr.setExpr ( "height=45" );
expr.setExpr ( "area=base*height/2" );
```
- Variables retrieval
  - Using name
```
Double base = expr.getVar ( "base" );
```
- Text parsing and expansion
```
expr.evaluateText ( "this is the average: ${output=average} and this is our final sum ${output=finalSum}", "output" );
```
  - Checking whether a variable is being used
```
if ( expr.isVarDefined ( "base" ) == true )
{
// VARIABLE "base" EXISTS
}
```
- User-defined functions handling
  - Registration
```
expr.registerFunction ( new Avg() );
```
  - Unregistration by function object
```
expr.unregisterFunction ( new Avg() );
```
  - Unregistration by function name
```
expr.unregisterFunction ( "avg" );
```
  - Registration check by function object
```
expr.isFunctionRegistered ( new Avg() );
```
  - Registration check by function name
```
expr.isFunctionRegistered ( "avg" );
```

### To do
- Support for more logical operators
- Improved support for conditional branching and loops
- Code cleanup
- ...

### ChangeLog

- 0.9 - Major enhancements: support for boolean operators and boolean bitwise operators (three subreleases for Java 1.4, 1.5 and 1.6) - October 6th, 2007
- 0.8 - Major enhancements: support for text with inline expressions expansion (three subreleases for Java 1.4, 1.5 and 1.6) - August 28th, 2007
- 0.7.1 - Minor enhancements: 0.7 release backported to be compatible with Java 1.4 - August 6th, 2007
- 0.7 - Minor enhancements: small bugfixes - July 30th, 2007
- 0.6 - Major enhancements: support for literal and string variables - November 8th, 2006
- 0.5 - Major enhancements: support for conditional and branch-case expressions - October 9th, 2006
- 0.4 - Minor enhancements: strict declarations according to Java 5 specifications added. Script to run the example code fixed - September 7th, 2006
- 0.3 - License changed from GPL to LGPL - July 27th, 2006
- 0.2 - Added support for dynamic registration/unregistration of user-defined functions - July 27th, 2006
- 0.1b - First public release - July 9th, 2006

### Related projects
- [STX Expression Parser C++ Framework](https://github.com/bingmann/stx-exparser "STX Expression Parser C++ Framework")
