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
import com.seovic.io.InputStreamUtils;

import java.util.Map;

import java.io.InputStream;

import groovy.lang.Script;
import groovy.lang.GroovyShell;
import groovy.lang.Binding;


/**
 * An imlementation of {@link Expression} that evaluates specified expression
 * using <a href="http://groovy.codehaus.org/" target="_new">Groovy</a>.
 * <p/>
 * Unlike the expression languages such as OGNL and MVEL, Groovy does not have
 * a notion of a "root object" for expression evaluation. Because of this, the
 * target object is bound to a variable called <tt>target</tt> and must be
 * referenced explicitly within the expression.
 * 
 * @author Aleksandar Seovic  2009.09.20
 */
@SuppressWarnings({"unchecked"})
public class GroovyExpression<T>
        extends AbstractExpression<T>
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public GroovyExpression()
        {
        }

    /**
     * Construct a <tt>GroovyExpression</tt> instance.
     *
     * @param expression  the script to evaluate
     */
    public GroovyExpression(String expression)
        {
        super(expression);
        }

    /**
     * Construct a <tt>GroovyExpression</tt> instance.
     *
     * @param script  the script to evaluate
     */
    public GroovyExpression(InputStream script)
        {
        m_expression = InputStreamUtils.readFullyAsString(script);
        }


    // ---- Expression implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public T evaluate(Object target, Map variables)
        {
        Binding binding = new Binding(variables);
        binding.setVariable("target", target);

        synchronized (monitor)
            {
            Script script = getCompiledScript();
            script.setBinding(binding);
            return (T) script.run();
            }
        }

    /**
     * {@inheritDoc}
     */
    public void evaluateAndSet(Object target, Object value)
        {
        Binding binding = new Binding();
        binding.setVariable("target", target);
        binding.setVariable("value", value);

        synchronized (monitor)
            {
            Script script = getCompiledSetScript();
            script.setBinding(binding);
            script.run();
            }
        }

    // ---- helper methods --------------------------------------------------

    /**
     * Return a compiled Groovy script for this expression.
     *
     * @return compiled Groovy script
     */
    protected Script getCompiledScript()
        {
        Script script = m_script;
        if (script == null)
            {
            GroovyShell shell = new GroovyShell();
            m_script = script = shell.parse(m_expression);
            }
        return script;
        }

    /**
     * Return a compiled Groovy script for this expression.
     *
     * @return compiled Groovy script
     */
    protected Script getCompiledSetScript()
        {
        Script script = m_setScript;
        if (script == null)
            {
            GroovyShell shell = new GroovyShell();
            m_setScript = script = shell.parse(m_expression + " = value");
            }
        return script;
        }

    // ---- data members ----------------------------------------------------

    /**
     * Monitor object.
     */
    private transient final Object monitor = new Object();

    /**
     * Compiled Groovy script
     */
    private transient Script m_script;

    /**
     * Compiled Groovy set script
     */
    private transient Script m_setScript;
    }