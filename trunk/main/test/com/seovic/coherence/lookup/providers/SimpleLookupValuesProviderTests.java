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
import org.junit.Before;

import static org.junit.Assert.*;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.filter.InFilter;

import com.seovic.test.objects.Country;

import com.seovic.coherence.lookup.LookupValuesProvider;
import com.seovic.coherence.lookup.LookupValueExtractor;
import com.seovic.coherence.lookup.LookupValue;

import com.seovic.lang.extractor.PropertyExtractor;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;


/**
 * Tests for SimpleLookupValuesProvider.
 * 
 * @author Aleksandar Seovic  2009.06.07
 */
@SuppressWarnings("unchecked")
public class SimpleLookupValuesProviderTests
    {
    private static NamedCache countries = CacheFactory.getCache("countries");

    @Before
    public void createTestData()
        {
        countries.clear();

        countries.put("SRB", new Country("SRB", "Serbia"));
        countries.put("SUI", new Country("SUI", "Switzerland"));
        countries.put("SWE", new Country("SWE", "Sweden"));
        countries.put("ESP", new Country("ESP", "Spain"));
        countries.put("USA", new Country("USA", "United States"));

        countries.addIndex(new PropertyExtractor("name"), true, null);
        }

    @Test
    public void testGetValues()
        {
        LookupValuesProvider<String, String> provider =
                new SimpleLookupValuesProvider(countries, new LookupValueExtractor("name"));

        Collection values = provider.getValues();

        assertEquals(5, values.size());
        assertTrue(values.contains(new LookupValue("SRB", "Serbia")));
        assertTrue(values.contains(new LookupValue("SUI", "Switzerland")));
        assertTrue(values.contains(new LookupValue("SWE", "Sweden")));
        assertTrue(values.contains(new LookupValue("ESP", "Spain")));
        assertTrue(values.contains(new LookupValue("USA", "United States")));
        }

    @Test
    public void testGetValuesFilteredByDescription()
        {
        LookupValuesProvider<String, String> provider =
                new SimpleLookupValuesProvider(countries, new LookupValueExtractor("name"));

        Collection values = provider.getValues("S", false);

        assertEquals(4, values.size());
        assertTrue(values.contains(new LookupValue("SRB", "Serbia")));
        assertTrue(values.contains(new LookupValue("SUI", "Switzerland")));
        assertTrue(values.contains(new LookupValue("SWE", "Sweden")));
        assertTrue(values.contains(new LookupValue("ESP", "Spain")));
        }

    @Test
    public void testGetValuesFilteredByDescriptionCaseInsensitive()
        {
        LookupValuesProvider<String, String> provider =
                new SimpleLookupValuesProvider(countries, new LookupValueExtractor("name"));

        Collection values = provider.getValues("sw", true);

        assertEquals(2, values.size());
        assertTrue(values.contains(new LookupValue("SUI", "Switzerland")));
        assertTrue(values.contains(new LookupValue("SWE", "Sweden")));
        }

    @Test
    public void testGetValuesFilteredByCustomFilter()
        {
        LookupValuesProvider<String, String> provider =
                new SimpleLookupValuesProvider(countries, new LookupValueExtractor("name"));

        Set codes = new HashSet();
        codes.add("USA");
        codes.add("SRB");
        codes.add("ESP");

        Collection values = provider.getValues(new InFilter("getCode", codes));

        assertEquals(3, values.size());
        assertTrue(values.contains(new LookupValue("SRB", "Serbia")));
        assertTrue(values.contains(new LookupValue("ESP", "Spain")));
        assertTrue(values.contains(new LookupValue("USA", "United States")));
        }
    }
