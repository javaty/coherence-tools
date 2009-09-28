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

package com.seovic.core.condition;


import com.seovic.core.Condition;
import com.seovic.core.Expression;
import com.seovic.core.Defaults;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.Serializable;
import java.io.IOException;


/**
 * An imlementation of {@link Condition} that evaluates boolean expression
 * using one of the {@link Expression} implementations.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class ExpressionCondition
        implements Condition, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public ExpressionCondition()
        {
        }

    /**
     * Construct an <tt>ExpressionCondition</tt> instance.
     *
     * @param expression  the expression to use
     */
    public ExpressionCondition(String expression)
        {
        this(Defaults.createExpression(expression));
        }

    /**
     * Construct an <tt>ExpressionCondition</tt> instance.
     *
     * @param expression  the expression to use
     */
    public ExpressionCondition(Expression expression)
        {
        m_expression = expression;
        }


    // ---- Condition implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(Object target)
        {
        try
            {
            return (Boolean) m_expression.evaluate(target);
            }
        catch (ClassCastException e)
            {
            throw new IllegalArgumentException(
                    "Specified expression does not evaluate to a boolean value");
            }
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize this object from a POF stream.
     *
     * @param reader  POF reader to use
     *
     * @throws IOException  if an error occurs during deserialization
     */
    public void readExternal(PofReader reader)
            throws IOException
        {
        m_expression = (Expression) reader.readObject(0);
        }

    /**
     * Serialize this object into a POF stream.
     *
     * @param writer  POF writer to use
     *
     * @throws IOException  if an error occurs during serialization
     */
    public void writeExternal(PofWriter writer)
            throws IOException
        {
        writer.writeObject(0, m_expression);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * Test objects for equality.
     *
     * @param o  object to compare this object with
     *
     * @return <tt>true</tt> if the specified object is equal to this object
     *         <tt>false</tt> otherwise
     */
    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        ExpressionCondition condition = (ExpressionCondition) o;
        return m_expression.equals(condition.m_expression);
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return m_expression.hashCode();
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "ExpressionCondition{" +
               "expression=" + m_expression +
               '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * The expression to use.
     */
    private Expression m_expression;
    }