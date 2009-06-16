package com.seovic.coherence.loader.properties;


import com.seovic.coherence.loader.PropertyGetter;
import com.seovic.expression.Expression;
import com.seovic.expression.ExpressionEngine;


/**
 * @author ic  2009.06.09
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ExpressionPropertyGetter
        implements PropertyGetter
    {
    private Expression expression;

    public ExpressionPropertyGetter(String expression)
        {
        this.expression = new ExpressionEngine().getExpressionParser().parse(
                expression);
        }

    public ExpressionPropertyGetter(String expression, String language)
        {
        this.expression = new ExpressionEngine().getExpressionParser().parse(
                expression);
        }

    public ExpressionPropertyGetter(Expression expression)
        {
        this.expression = expression;
        }

    public Object getValue(Object sourceItem)
        {
        return expression.getValue(sourceItem);
        }
    }
