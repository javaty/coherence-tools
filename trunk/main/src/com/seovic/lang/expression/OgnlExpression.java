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

package com.seovic.lang.expression;


import com.seovic.lang.Expression;

import ognl.Ognl;
import ognl.OgnlException;
import java.util.Map;


/**
 * An imlementation of {@link Expression} that evaluates specified expression
 * using <a href="http://www.opensymphony.com/ognl/" target="_new">OGNL</a>.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class OgnlExpression
        extends AbstractExpression
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public OgnlExpression()
        {
        }

    /**
     * Construct a <tt>OgnlExpression</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
    public OgnlExpression(String expression)
        {
        super(expression);
        }


    // ---- Expression implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object eval(Object target, Map variables)
        {
        try
            {
            return variables == null
                    ? Ognl.getValue(getCompiledExpression(), target)
                    : Ognl.getValue(getCompiledExpression(), variables, target);
            }
        catch (OgnlException e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Return a compiled OGNL expression.
     *
     * @return compiled OGNL expression
     */
    protected Object getCompiledExpression()
        {
        Object compiledExpression = m_compiledExpression;
        if (compiledExpression == null)
            {
            try
                {
                m_compiledExpression = compiledExpression =
                        Ognl.parseExpression(m_expression);
                }
            catch (OgnlException e)
                {
                throw new IllegalArgumentException("[" + m_expression +
                                                   "] is not a valid OGNL expression");
                }
            }
        return compiledExpression;
        }


    // ---- data members ----------------------------------------------------

    /**
     * Compiled OGNL expression
     */
    private transient Object m_compiledExpression;
    }