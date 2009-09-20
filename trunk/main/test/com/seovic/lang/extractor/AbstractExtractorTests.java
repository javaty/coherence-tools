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


import static org.junit.Assert.*;

import org.junit.Test;

import com.seovic.lang.Extractor;
import com.seovic.test.objects.Person;
import com.seovic.test.objects.Address;


/**
 * Common expression tests.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
@SuppressWarnings({"unchecked", "RedundantCast"})
public abstract class AbstractExtractorTests
    {
    protected abstract Extractor createExtractor(String expression);
    protected abstract String getName();

    @Test(expected = RuntimeException.class)
    public void testWithBadProperty()
        {
        Person    person = new Person(1L, "Homer");
        Extractor ext    = createExtractor("sjdhfgw");
        ext.extract(person);
        }

    @Test
    public void testNullTargetExtraction()
        {
        Extractor ext = createExtractor("name");
        assertNull(ext.extract(null));
        }

    @Test
    public void testSimplePropertyExtraction()
        {
        Person    person = createTestTarget();
        Extractor ext    = createExtractor("name");
        assertEquals("Homer", ext.extract(person));
        }

    @Test
    public void testNestedPropertyExtraction()
        {
        Person    person = createTestTarget();
        Extractor ext    = createExtractor("address.city");
        assertEquals("Springfield", ext.extract(person));
        }

    @Test
    public void testPerformance()
        {
        Person    person = new Person(1L, "Homer");
        Extractor ext    = createExtractor("name");

        final int COUNT = 1000000;

        // warm up
        for (int i = 0; i < 1000; i++)
            {
            ext.extract(person);
            }

        // test
        long start = System.nanoTime();
        for (int i = 0; i < COUNT; i++)
            {
            ext.extract(person);
            }
        long duration = System.nanoTime() - start;

        System.out.println(getName() + ": " + duration / 1000000 + " ms");
        }


    // ---- helper methods --------------------------------------------------

    protected Person createTestTarget()
        {
        return new Person(1L, "Homer", null,
                          new Address("111 Main St", "Springfield", "USA"));
        }
    }