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


import static org.junit.Assert.*;
import org.junit.Test;

import com.seovic.test.objects.Person;

import java.util.Date;


/**
 * Tests for {@link OgnlExtractor}.
 *
 * @author ic  2009.06.16
 */
public class OgnlExtractorTests
    {

    @Test(expected = RuntimeException.class)
    public void testWithBadProperty()
        {
        OgnlExtractor extractor = new OgnlExtractor("bad");
        Person ivan = new Person("Ivan", 2504l, null, null);
        extractor.extract(ivan);
        }

    @Test
    public void testWithComplexType()
        {
        OgnlExtractor extractor = new OgnlExtractor("address.city");
        Person.Address address = new Person.Address("Merced", "Santiago", "Chile");
        Person ivan = new Person("Ivan", 2504l, new Date(), address);
        assertEquals("Santiago", extractor.extract(ivan));
        }

    @Test
    public void testWithExpressionTransformation()
        {
        OgnlExtractor extractor = new OgnlExtractor("name + ':' + idNo");
        Person ivan = new Person("Ivan", 2504l, null, null);
        assertEquals("Ivan:2504", extractor.extract(ivan));
        extractor = new OgnlExtractor("name == 'Ivan'");
        assertTrue((Boolean) extractor.extract(ivan));
        }
    }
