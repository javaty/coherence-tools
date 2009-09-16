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
import ognl.Ognl;
import ognl.OgnlException;


/**
 * An imlementation of {@link Extractor} that extracts value from a
 * target object using <a href="http://www.opensymphony.com/ognl/" target="_new">
 * OGNL</a> expression.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class OgnlExtractor
        implements Extractor {
    // ---- data members ----------------------------------------------------

    private Object expression;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>OgnlExtractor</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
    public OgnlExtractor(String expression) {
        try {
            this.expression = Ognl.parseExpression(expression);
        }
        catch (OgnlException e) {
            throw new IllegalArgumentException("[" + expression +
                                               "] is not a valid OGNL expression");
        }
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }

        try {
            return Ognl.getValue(expression, target);
        }
        catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }
}