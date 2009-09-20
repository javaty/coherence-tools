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

import com.seovic.lang.expression.MvelExpression;


/**
 * An imlementation of {@link Condition} that evaluates boolean expression
 * using <a href="http://mvel.codehaus.org/" target="_new">MVEL</a> and returns
 * the result.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class MvelCondition
        extends    MvelExpression
        implements Condition
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public MvelCondition()
        {
        }

    /**
     * Construct a <tt>MvelCondition</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
    public MvelCondition(String expression)
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
            throw new IllegalArgumentException("Specified expression does not"
                                               + "evaluate to a boolean value");
            }
        }
    }