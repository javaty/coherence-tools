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

package com.seovic.util.extractors;


import com.seovic.util.Extractor;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.Compilable;
import javax.script.SimpleBindings;


/**
 * An imlementation of {@link Extractor} that extracts value from a
 * target object using a script written in any language supported by the Java
 * Scripting API (JSR-223).
 * <p/>
 * The target object can be accessed within the script using <tt>target</tt>
 * variable binding.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class ScriptExtractor
        implements Extractor {
    // ---- data members ----------------------------------------------------

    private String language;
    private Reader scriptReader;

    private transient ScriptEngine engine;
    private transient CompiledScript script;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>ScriptExtractor</tt> instance.
     *
     * @param language  script language (i.e. javascript or groovy)
     * @param script    the script to evaluate
     */
    public ScriptExtractor(String language, String script) {
        this.language     = language;
        this.scriptReader = new StringReader(script);
    }

    /**
     * Construct a <tt>ScriptExtractor</tt> instance.
     *
     * @param language  script language (i.e. javascript or groovy)
     * @param script    the script to evaluate
     */
    public ScriptExtractor(String language, InputStream script) {
        this.language     = language;
        this.scriptReader = new InputStreamReader(script);
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }

        SimpleBindings bindings = new SimpleBindings();
        bindings.put("target", target);

        try {
            CompiledScript script = getCompiledScript();
            return script == null
                    ? getScriptEngine().eval(scriptReader, bindings)
                    : script.eval(bindings);
        }
        catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    protected ScriptEngine getScriptEngine() {
        if (engine == null) {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName(language);
        }
        return engine;
    }

    protected CompiledScript getCompiledScript()
            throws ScriptException {
        if (script != null) {
            return script;
        }
        else {
            ScriptEngine engine = getScriptEngine();
            if (engine instanceof Compilable) {
                script = ((Compilable) engine).compile(scriptReader);
                return script;
            }
        }
        return null;
    }
}