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

package com.seovic.core.updater;

import static org.junit.Assert.*;
import org.junit.Test;

import com.seovic.test.objects.Person;
import com.seovic.test.objects.Address;


/**
 * Tests for {BeanWrapperPropertySetter}.
 *
 * @author ic  2009.06.16
 */
public class BeanWrapperUpdaterTests
    {
    @Test(expected = RuntimeException.class)
    public void testWithBadPropertyName()
        {
        BeanWrapperUpdater updater = new BeanWrapperUpdater("bad");
        Person person = new Person();
        updater.update(person, "value");
        }

    @Test
    public void testWithStringPropertyType()
        {
        BeanWrapperUpdater updater = new BeanWrapperUpdater("name");
        Person person = new Person();
        assertNull(person.getName());
        
        updater.update(person, "Ivan");
        assertEquals("Ivan", person.getName());
        }

    @Test
    public void testWithPrimitivePropertyType()
        {
        BeanWrapperUpdater updater = new BeanWrapperUpdater("id");
        Person person = new Person();
        assertEquals(0L, person.getId());

        updater.update(person, 2504L);
        assertEquals(2504L, person.getId());
        }

    @Test
    public void testWithComplexPropertyType()
        {
        BeanWrapperUpdater updater = new BeanWrapperUpdater("address");
        Person person = new Person();
        assertNull(person.getAddress());

        Address merced = new Address("Merced", "Santiago", "Chile");
        updater.update(person, merced);
        assertEquals(merced, person.getAddress());

        updater = new BeanWrapperUpdater("address.street");
        updater.update(person, "Av Bernardo O'Higgins");
        assertEquals("Av Bernardo O'Higgins", person.getAddress().getStreet());
        }
    }
