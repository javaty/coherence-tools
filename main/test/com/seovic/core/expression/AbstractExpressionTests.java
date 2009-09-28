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


import static org.junit.Assert.*;

import org.junit.Test;

import com.seovic.test.objects.Person;
import com.seovic.test.objects.Address;

import com.seovic.core.Expression;

import java.util.Map;
import java.util.HashMap;


/**
 * Common expression tests.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
@SuppressWarnings({"unchecked", "RedundantCast"})
public abstract class AbstractExpressionTests
    {
    protected abstract Expression createExpression(String expression);
    protected abstract String getLanguage();

    @Test
    public void testLiteralExpression()
        {
        Expression exp = createExpression("5 + 5");
        assertEquals(new Integer(10), (Integer) exp.evaluate(null));
        }

    @Test
    public void testSimplePropertyExpression()
        {
        Person     person = new Person(1L, "Homer");
        Expression exp    = createExpression("name");
        assertEquals("Homer", exp.evaluate(person));
        }

    @Test
    public void testNestedPropertyExpression()
        {
        Person     person = new Person(1L, "Homer", null,
                                       new Address("111 Main St", "Springfield", "USA"));
        Expression exp    = createExpression("address.city");
        assertEquals("Springfield", exp.evaluate(person));
        }

    @Test
    public void testExpressionWithVariables()
        {
        Map vars = new HashMap();
        vars.put("x", 5);
        vars.put("y", 5);

        Expression exp = createExpression("x * y");
        assertEquals(new Integer(25), (Integer) exp.evaluate(null, vars));
        }

    @Test
    public void testExpressionWithTargetAndVariables()
        {
        Person person = new Person(1L, "Homer");
        Map    vars   = new HashMap();
        vars.put("lastName", "Simpson");

        Expression exp = createExpression("name + ' ' + lastName");
        assertEquals("Homer Simpson", exp.evaluate(person, vars));
        }

    @Test
    public void testPerformance()
        {
        Person     person = new Person(1L, "Homer");
        Expression exp    = createExpression("name");

        final int COUNT = 1000000;

        // warm up
        for (int i = 0; i < 1000; i++)
            {
            exp.evaluate(person);
            }

        // test
        long start = System.nanoTime();
        for (int i = 0; i < COUNT; i++)
            {
            exp.evaluate(person);
            }
        long duration = System.nanoTime() - start;

        System.out.println(getLanguage() + ": " + duration / 1000000 + " ms");
        }
    }
