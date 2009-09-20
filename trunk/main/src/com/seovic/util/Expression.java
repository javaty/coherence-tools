package com.seovic.util;


import java.util.Map;


/**
 * Expression interface represents an expression that can be evaluated against
 * the target object.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public interface Expression
    {
    /**
     * Evaluates expression against the target object.
     *
     * @param target  object to evaluate expression against
     *
     * @return expression result
     */
    public Object eval(Object target);

    /**
     * Evaluates expression against the target object.
     *
     * @param target     object to evaluate expression against
     * @param variables  variables to use during evaluation
     *
     * @return expression result
     */
    public Object eval(Object target, Map variables);
    }
