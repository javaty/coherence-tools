/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.coherence.loader.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import com.seovic.coherence.test.objects.Person;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;


/**
 * Tests for {@link MapPropertyGetter}
 * 
 * @author ic  2009.06.16
 */
public class MapPropertyGetterTests
    {

    @Test
    public void testWithBadProperty()
        {
        MapPropertyGetter getter = new MapPropertyGetter("bad");
        Map<String, Object> source = createTestSourceMap();
        assertNull(getter.getValue(source));
        }

    @Test
    public void testWithNullProperty()
        {
        MapPropertyGetter getter = new MapPropertyGetter(null);
        Map<String, Object> source = createTestSourceMap();
        assertNull(getter.getValue(source));
        }


    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTarget()
        {
        MapPropertyGetter getter = new MapPropertyGetter("prop");
        getter.getValue(null);
        }

    @Test
    public void testWithExistingProperty()
        {
        MapPropertyGetter getter = new MapPropertyGetter("address");
        Person.Address merced = new Person.Address("Merced", "Santiago", "Chile");
        Map<String, Object> sourceItem = createTestSourceMap();
        assertEquals(merced, getter.getValue(sourceItem));
        getter = new MapPropertyGetter("dob");
        assertTrue(getter.getValue(sourceItem) instanceof Date);
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
