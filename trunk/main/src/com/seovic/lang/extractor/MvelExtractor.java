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
import com.seovic.lang.expression.MvelExpression;


/**
 * An imlementation of {@link Extractor} that extracts value from a target
 * object using <a href="http://mvel.codehaus.org/" target="_new">MVEL</a>
 * expression.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class MvelExtractor
        extends MvelExpression
        implements Extractor
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public MvelExtractor()
        {
        }

    /**
     * Construct a <tt>MvelExtractor</tt> instance.
     *
     * @param expression the expression to evaluate
     */
    public MvelExtractor(String expression)
        {
        super(expression);
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