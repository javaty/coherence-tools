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
import com.seovic.core.expression.ScriptExpression;


/**
 * An imlementation of {@link Extractor} that extracts value from a target
 * object using {@link ScriptExpression}.
 *
 * @author Aleksandar Seovic  2009.11.07
 */
public class ScriptExtractor
        extends ExpressionExtractor
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public ScriptExtractor()
        {
        }

    /**
     * Construct an <tt>ScriptExtractor</tt> instance.
     *
     * @param expression  the expression to use
     */
    public ScriptExtractor(String expression)
        {
        super(new ScriptExpression(expression));
        }

    /**
     * Construct an <tt>ScriptExtractor</tt> instance.
     *
     * @param expression  the expression to evaluate
     * @param language    scripting language to use
     */
    public ScriptExtractor(String expression, String language)
        {
        super(new ScriptExpression(expression, language));
        }
    }