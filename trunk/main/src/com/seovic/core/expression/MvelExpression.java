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

package com.seovic.core.expression;


import com.seovic.core.Expression;

import org.mvel2.MVEL;

import java.util.Map;

import java.io.Serializable;


/**
 * An imlementation of {@link Expression} that evaluates specified expression
 * using <a href="http://mvel.codehaus.org/" target="_new">MVEL</a>.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class MvelExpression
        extends AbstractExpression
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public MvelExpression()
        {
        }

    /**
     * Construct a <tt>MvelExpression</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
    public MvelExpression(String expression)
        {
        super(expression);
        }


    // ---- Expression implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object evaluate(Object target, Map variables)
        {
        return MVEL.executeExpression(getCompiledExpression(), target, variables);
        }

    /**
     * {@inheritDoc}
     */
    public void evaluateAndSet(Object target, Object value)
        {
        MVEL.executeSetExpression(getCompiledSetExpression(), target, value);
        }

    // ---- helper methods --------------------------------------------------

    /**
     * Return a compiled MVEL expression.
     *
     * @return compiled MVEL expression
     */
    protected Serializable getCompiledExpression()
        {
        Serializable compiledExpression = m_compiledExpression;
        if (compiledExpression == null)
            {
            m_compiledExpression = compiledExpression =
                    MVEL.compileExpression(m_expression);
            }
        return compiledExpression;
        }

    /**
     * Return a compiled MVEL set expression.
     *
     * @return compiled MVEL set expression
     */
    protected Serializable getCompiledSetExpression()
        {
        Serializable compiledExpression = m_compiledSetExpression;
        if (compiledExpression == null)
            {
            m_compiledSetExpression = compiledExpression =
                    MVEL.compileSetExpression(m_expression);
            }
        return compiledExpression;
        }

    // ---- data members ----------------------------------------------------

    /**
     * Compiled expression.
     */
    private transient Serializable m_compiledExpression;

    /**
     * Compiled set expression.
     */
    private transient Serializable m_compiledSetExpression;
    }