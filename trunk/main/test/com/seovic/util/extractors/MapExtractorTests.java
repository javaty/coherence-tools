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
import java.util.Map;
import java.util.HashMap;


/**
 * Tests for {@link MapExtractor}
 * 
 * @author ic  2009.06.16
 */
public class MapExtractorTests
    {

    @Test
    public void testWithBadProperty()
        {
        MapExtractor extractor = new MapExtractor("bad");
        Map<String, Object> source = createTestSourceMap();
        assertNull(extractor.extract(source));
        }

    @Test
    public void testWithNullProperty()
        {
        MapExtractor extractor = new MapExtractor(null);
        Map<String, Object> source = createTestSourceMap();
        assertNull(extractor.extract(source));
        }


    @Test
    public void testWithNullTarget()
        {
        MapExtractor extractor = new MapExtractor("prop");
        assertNull(extractor.extract(null));
        }

    @Test
    public void testWithExistingProperty()
        {
        MapExtractor extractor = new MapExtractor("address");
        Person.Address merced = new Person.Address("Merced", "Santiago", "Chile");
        Map<String, Object> sourceItem = createTestSourceMap();
        assertEquals(merced, extractor.extract(sourceItem));
        extractor = new MapExtractor("dob");
        assertTrue(extractor.extract(sourceItem) instanceof Date);
        }

    private Map<String, Object> createTestSourceMap()
        {
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("name", "Ivan");
        source.put("idNo", 2504);
        source.put("dob", new Date());
        source.put("address", new Person.Address("Merced", "Santiago", "Chile"));
        return source;
        }
    }
