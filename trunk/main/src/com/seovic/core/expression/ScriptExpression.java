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
import com.seovic.config.Configuration;
import com.seovic.io.InputStreamUtils;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.util.Map;

import java.io.InputStream;
import java.io.IOException;

import javax.script.SimpleBindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.Compilable;


/**
 * An imlementation of {@link Expression} that evaluates specified expression
 * using any supported scripting language.
 * <p/>
 * Unlike the expression languages such as OGNL and MVEL, scripting languages do
 * not have a notion of a "root object" for expression evaluation. Because of
 * this, the target object is bound to a variable called <tt>target</tt> and
 * must be referenced explicitly within the expression.
 *
 * @author Aleksandar Seovic  2009.09.27
 */
@SuppressWarnings({"unchecked"})
public class ScriptExpression<T>
        extends AbstractExpression<T>
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public ScriptExpression()
        {
        }

    /**
     * Construct a <tt>ScriptExpression</tt> instance.
     *
     * @param expression the script to evaluate
     */
    public ScriptExpression(String expression)
        {
        this(expression, Configuration.getDefaultScriptLanguage());
        }

    /**
     * Construct a <tt>ScriptExpression</tt> instance.
     *
     * @param script   the script to evaluate
     */
    public ScriptExpression(InputStream script)
        {
        this(script, Configuration.getDefaultScriptLanguage());
        }

    /**
     * Construct a <tt>ScriptExpression</tt> instance.
     *
     * @param expression  the script to evaluate
     * @param language    scripting language to use
     */
    public ScriptExpression(String expression, String language)
        {
        super(expression);
        m_language = language;
        }

    /**
     * Construct a <tt>ScriptExpression</tt> instance.
     *
     * @param script    the script to evaluate
     * @param language  scripting language to use
     */
    public ScriptExpression(InputStream script, String language)
        {
        super(InputStreamUtils.readFullyAsString(script));
        m_language = language;
        }


    // ---- Expression implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public T evaluate(Object target, Map variables)
        {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("target", target);

        try
            {
            CompiledScript script = getCompiledScript();
            return (T) (script == null
                               ? getScriptEngine().eval(m_expression, bindings)
                               : script.eval(bindings));
            }
        catch (ScriptException e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    public void evaluateAndSet(Object target, Object value)
        {
        throw new UnsupportedOperationException(
                "ScriptExpression cannot be used to update target object value.");
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Return script engine for this expression's language.
     *
     * @return script engine
     */
    protected ScriptEngine getScriptEngine()
        {
        ScriptEngine engine = m_engine;
        if (engine == null)
            {
            ScriptEngineManager manager = new ScriptEngineManager();
            m_engine = engine = manager.getEngineByName(m_language);
            }
        return engine;
        }

    /**
     * Return a compiled script for this expression.
     *
     * @return compiled script
     *
     * @throws ScriptException  if a compilation error occurs
     */
    protected CompiledScript getCompiledScript()
            throws ScriptException
        {
        CompiledScript script = m_script;
        if (script != null)
            {
            return script;
            }
        else
            {
            ScriptEngine engine = getScriptEngine();
            if (engine instanceof Compilable)
                {
                m_script = script = ((Compilable) engine).compile(m_expression);
                return script;
                }
            }
        return null;
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
        super.readExternal(reader);
        m_language = reader.readString(10);
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
        super.writeExternal(writer);
        writer.writeString(10, m_language);
        }


    // ---- data members ----------------------------------------------------

    /**
     * Default language.
     */
    public static final String DEFAULT_LANGUAGE = "javascript";

    /**
     * Script language.
     */
    private String m_language;

    /**
     * Scripting engine to use.
     */
    private transient ScriptEngine m_engine;

    /**
     * Compiled script
     */
    private transient CompiledScript m_script;
    }