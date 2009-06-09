package com.seovic.coherence.loader.mapper;

import com.seovic.expression.Expression;
import com.seovic.expression.ExpressionEngine;
import com.seovic.coherence.loader.PropertyMapper;

/**
 * @author ic  2009.06.09
 */
public class ExpressionPropertyMapper implements PropertyMapper {
    private Expression expression;

    public ExpressionPropertyMapper(String expression) {
        this.expression = new ExpressionEngine().getExpressionParser().parse(expression);
    }

    public ExpressionPropertyMapper(String expression, String language) {
        this.expression = new ExpressionEngine().getExpressionParser().parse(expression);
    }

    public ExpressionPropertyMapper(Expression expression) {
        this.expression = expression;
    }

    public Object getValue(Object item) {
        return expression.getValue(item);
    }
}
