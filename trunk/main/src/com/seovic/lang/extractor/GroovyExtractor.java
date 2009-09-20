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

package com.seovic.lang.extractor;


import com.seovic.lang.Extractor;
import com.seovic.lang.expression.GroovyExpression;

import java.io.InputStream;


/**
 * An imlementation of {@link Extractor} that extracts value from a target
 * object using <a href="http://groovy.codehaus.org/" target="_new"> Groovy</a>
 * script.
 * <p/>
 * The target object can be accessed within the script using <tt>target</tt>
 * variable binding.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class GroovyExtractor
        extends    GroovyExpression
        implements Extractor
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public GroovyExtractor()
        {
        }

    /**
     * Construct a <tt>GroovyExtractor</tt> instance.
     *
     * @param script the script to evaluate
     */
    public GroovyExtractor(String script)
        {
        super(script);
        }

    /**
     * Construct a <tt>GroovyExtractor</tt> instance.
     *
     * @param script the script to evaluate
     */
    public GroovyExtractor(InputStream script)
        {
        super(script);
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
        return super.eval(target);
        }
    }