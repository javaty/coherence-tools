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

package com.seovic.coherence.lookup.providers;

import org.junit.Test;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LikeFilter;
import com.tangosol.util.Filter;

import com.seovic.test.objects.City;

import com.seovic.coherence.lookup.LookupValuesProvider;
import com.seovic.coherence.lookup.LookupValueExtractor;
import com.seovic.coherence.lookup.LookupValue;

import com.seovic.lang.extractor.PropertyExtractor;

import java.util.Collection;


/**
 * Tests for DependentLookupValuesProvider.
 *
 * @author Aleksandar Seovic  2009.06.07
 */
@SuppressWarnings("unchecked")
public class DependentLookupValuesProviderTests
    {
    private static NamedCache cities    = CacheFactory.getCache("cities");
    private static Filter     depFilter = new EqualsFilter(new PropertyExtractor("countryCode"), "SRB");

    @BeforeClass
    public static void createTestData()
        {
        cities.put(1, new City(1, "SRB", "Beograd"));
        cities.put(2, new City(2, "SRB", "Ni?"));
        cities.put(3, new City(3, "SRB", "Novi Sad"));
        cities.put(4, new City(4, "USA", "Tampa"));
        cities.put(5, new City(5, "USA", "Chicago"));
        cities.put(6, new City(6, "USA", "Boston"));
        cities.put(7, new City(7, "USA", "New York"));

        cities.addIndex(new PropertyExtractor("name"), true, null);
        }

    @Test
    public void testGetValues()
        {
        LookupValuesProvider<Long, String> provider =
                new DependentLookupValuesProvider(cities,
                                                  new LookupValueExtractor("name"),
                                                  depFilter);

        Collection values = provider.getValues();

        assertEquals(3, values.size());
        assertTrue(values.contains(new LookupValue(1, "Beograd")));
        assertTrue(values.contains(new LookupValue(2, "Ni?")));
        assertTrue(values.contains(new LookupValue(3, "Novi Sad")));
        }

    @Test
    public void testGetValuesFilteredByDescription()
        {
        LookupValuesProvider<Long, String> provider =
                new DependentLookupValuesProvider(cities,
                                                  new LookupValueExtractor("name"),
                                                  depFilter);

        Collection values = provider.getValues("N", false);

        assertEquals(2, values.size());
        assertTrue(values.contains(new LookupValue(2, "Ni?")));
        assertTrue(values.contains(new LookupValue(3, "Novi Sad")));
        }

    @Test
    public void testGetValuesFilteredByDescriptionCaseInsensitive()
        {
        LookupValuesProvider<Long, String> provider =
                new DependentLookupValuesProvider(cities,
                                                  new LookupValueExtractor("name"),
                                                  depFilter);

        Collection values = provider.getValues("nov", true);

        assertEquals(1, values.size());
        assertTrue(values.contains(new LookupValue(3, "Novi Sad")));
        }

    @Test
    public void testGetValuesFilteredByCustomFilter()
        {
        LookupValuesProvider<Long, String> provider =
                new DependentLookupValuesProvider(cities,
                                                  new LookupValueExtractor("name"),
                                                  depFilter);

        Collection values = provider.getValues(new LikeFilter("getName", "%ad"));

        assertEquals(2, values.size());
        assertTrue(values.contains(new LookupValue(1, "Beograd")));
        assertTrue(values.contains(new LookupValue(3, "Novi Sad")));
        }
    }