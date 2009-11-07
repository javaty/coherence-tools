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

package com.seovic.core.extractor;


import com.seovic.core.Extractor;
import com.seovic.core.Expression;
import com.seovic.core.Defaults;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.Serializable;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;


/**
 * An imlementation of {@link Extractor} that extracts value from a target
 * object using one of the {@link Expression} implementations.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class ExpressionExtractor
        implements Extractor, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public ExpressionExtractor()
        {
        }

    /**
     * Construct an <tt>ExpressionExtractor</tt> instance.
     *
     * @param expression  the expression to use
     */
    public ExpressionExtractor(String expression)
        {
        this(Defaults.createExpression(expression));
        }

    /**
     * Construct an <tt>ExpressionExtractor</tt> instance.
     *
     * @param expression  the expression to use
     */
    public ExpressionExtractor(Expression expression)
        {
        this(expression, null);
        }

    /**
     * Construct an <tt>ExpressionExtractor</tt> instance.
     *
     * @param expression  the expression to use
     * @param variables   the map containing variables to be used during evaluation
     */
    public ExpressionExtractor(Expression expression, Map variables)
        {
        m_expression = expression;
        m_variables  = variables;
        }

    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target)
        {
        if (target == null)
            {
            return null;
            }
        return m_expression.evaluate(target, m_variables);
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
        m_variables  = reader.readMap(1, new HashMap());
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
        writer.writeMap(1, m_variables);
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

        ExpressionExtractor that = (ExpressionExtractor) o;

        return m_expression.equals(that.m_expression)
                && (m_variables == null
                        ? that.m_variables == null
                        : m_variables.equals(that.m_variables));

        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        int result = m_expression.hashCode();
        result = 31 * result + (m_variables != null ? m_variables.hashCode() : 0);
        return result;
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return getClass().getSimpleName() + "{" +
                "m_expression=" + m_expression +
                ", m_variables=" + m_variables +
                '}';
        }

    
    // ---- data members ----------------------------------------------------

    /**
     * The expression to use.
     */
    private Expression m_expression;

    /**
     *  The map containing variables to use during evaluation.
     */
    private Map        m_variables;

    }