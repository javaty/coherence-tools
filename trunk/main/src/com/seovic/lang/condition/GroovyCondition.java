/*
 * Copyright 2009 Aleksandar Seovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seovic.lang.condition;


import com.seovic.lang.Condition;

import com.seovic.lang.expression.GroovyExpression;


/**
 * An imlementation of {@link Condition} that evaluates boolean expression
 * using <a href="http://groovy.codehaus.org/" target="_new">Groovy</a>.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class GroovyCondition
        extends    GroovyExpression
        implements Condition
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public GroovyCondition()
        {
        }

    /**
     * Construct a <tt>GroovyCondition</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
    public GroovyCondition(String expression)
        {
        super(expression);
        }


    // ---- Condition implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(Object target)
        {
        try
            {
            return (Boolean) super.eval(target);
            }
        catch (ClassCastException e)
            {
            throw new IllegalArgumentException(
                    "Specified expression does not evaluate to a boolean value");
            }
        }
    }