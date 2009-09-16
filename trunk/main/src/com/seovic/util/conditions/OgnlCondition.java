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

package com.seovic.util.conditions;


import com.seovic.util.Condition;

import ognl.Ognl;
import ognl.OgnlException;


/**
 * An imlementation of {@link Condition} that evaluates boolean expression
 * using <a href="http://www.opensymphony.com/ognl/" target="_new">OGNL</a>
 * and returns the result.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class OgnlCondition
        implements Condition {
    // ---- data members ----------------------------------------------------

    private Object expression;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>OgnlCondition</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
    public OgnlCondition(String expression) {
        try {
            this.expression = Ognl.parseExpression(expression);
        }
        catch (OgnlException e) {
            throw new IllegalArgumentException("[" + expression +
                                               "] is not a valid OGNL expression");
        }
    }


    // ---- Condition implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(Object target) {
        try {
            return (Boolean) Ognl.getValue(expression, target);
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException("Specified expression does not"
                                               + "evaluate to a boolean value");
        }
        catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }
}