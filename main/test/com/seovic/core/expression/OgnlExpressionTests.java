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


/**
 * OgnlExpression tests.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class OgnlExpressionTests
        extends AbstractExpressionTests
    {
    protected Expression createExpression(String expression)
        {
        if ("x * y".equals(expression))                 expression = "#x * #y";
        if ("name + ' ' + lastName".equals(expression)) expression = "name + ' ' + #lastName";
        return new OgnlExpression(expression);
        }

    protected String getLanguage()
        {
        return "OGNL";
        }
    }
