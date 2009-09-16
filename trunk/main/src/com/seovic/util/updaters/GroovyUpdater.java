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

package com.seovic.util.updaters;


import com.seovic.util.Updater;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.lang.Binding;

import java.io.InputStream;


/**
 * An imlementation of {@link Updater} that updates target object using
 * <a href="http://groovy.codehaus.org/" target="_new">Groovy</a> script.
 * <p/>
 * The target object can be accessed within the script using <tt>target</tt>
 * variable binding, and the update value can be accessed using <tt>value</tt>
 * variable binding.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class GroovyUpdater
        implements Updater {
    // ---- data members ----------------------------------------------------

    private final Script script;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>GroovyUpdater</tt> instance.
     *
     * @param script  the script to evaluate
     */
    public GroovyUpdater(String script) {
        GroovyShell shell = new GroovyShell();
        this.script = shell.parse(script);
    }

    /**
     * Construct a <tt>GroovyUpdater</tt> instance.
     *
     * @param script  the script to evaluate
     */
    public GroovyUpdater(InputStream script) {
        GroovyShell shell = new GroovyShell();
        this.script = shell.parse(script);
    }


    // ---- Updater implementation ------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void update(Object target, Object value) {
        if (target == null) {
            throw new IllegalArgumentException("Updater target cannot be null");
        }

        Binding binding = new Binding();
        binding.setVariable("target", target);
        binding.setVariable("value",  value);

        synchronized (script) {
            script.setBinding(binding);
            script.run();
        }
    }
}