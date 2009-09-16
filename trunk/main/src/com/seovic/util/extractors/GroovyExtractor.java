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

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.lang.Binding;

import java.io.InputStream;


/**
 * An imlementation of {@link Extractor} that extracts value from a
 * target object using <a href="http://groovy.codehaus.org/" target="_new">
 * Groovy</a> script.
 * <p/>
 * The target object can be accessed within the script using <tt>target</tt>
 * variable binding.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class GroovyExtractor
        implements Extractor {
    // ---- data members ----------------------------------------------------

    private final Script script;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>GroovyExtractor</tt> instance.
     *
     * @param script  the script to evaluate
     */
    public GroovyExtractor(String script) {
        GroovyShell shell = new GroovyShell();
        this.script = shell.parse(script);
    }

    /**
     * Construct a <tt>GroovyExtractor</tt> instance.
     *
     * @param script  the script to evaluate
     */
    public GroovyExtractor(InputStream script) {
        GroovyShell shell = new GroovyShell();
        this.script = shell.parse(script);
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }

        Binding binding = new Binding();
        binding.setVariable("target", target);

        synchronized (script) {
            script.setBinding(binding);
            return script.run();
        }
    }
}