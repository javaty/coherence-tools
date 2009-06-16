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


import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;
import com.seovic.coherence.test.objects.Person;


/**
 * Tests for {@link MapPropertySetter}.
 * 
 * @author ic  2009.06.16
 */
public class MapPropertySetterTests
    {
    @Test(expected = IllegalArgumentException.class)
    public void testWithNullProperty()
        {
        MapPropertySetter setter = new MapPropertySetter(null);
        Map<String, Object> target = new HashMap<String, Object>();
        setter.setValue(target, "KEYED_BY_NULL");
        }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTarget()
        {
        MapPropertySetter setter = new MapPropertySetter("prop");
        setter.setValue(null, "PROP_VALUE");
        }

    @Test
    public void testWithNonNullProperty()
        {
        MapPropertySetter setter = new MapPropertySetter("address");
        Person.Address merced = new Person.Address("Merced", "Santiago", "Chile");
        Map<String, Object> target = new HashMap<String, Object>();
        setter.setValue(target, merced);
        assertEquals(merced, target.get("address"));
        }
    }
