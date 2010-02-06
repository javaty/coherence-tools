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

package com.seovic.coherence.util.filter;

import com.seovic.coherence.loader.CsvToCoherence;
import com.seovic.test.objects.Country;
import com.seovic.core.Condition;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.Set;


@SuppressWarnings("unchecked")
public class FilterBuilderTests
    {
    private static NamedCache countries = CacheFactory.getCache("countries");

    @Before
    public void before() throws Exception {
        countries.clear();
        loadCountries("countries.csv");
    }

    @Test
    public void testFilterBuilderUsingSimplePropertyExpressions() {
        FilterBuilder fb = new FilterBuilder()
                .equals("currencySymbol", "EUR")
                .like("name", "B%", false);

        Condition and = fb.toAnd();
        Set keys = countries.keySet(and);
        assertEquals(1, keys.size());

        Condition or = fb.toOr();
        keys = countries.keySet(or);
        assertEquals(45, keys.size());
    }

    @Test
    public void testFilterBuilderUsingConditionalExpressions() {
        FilterBuilder fb = new FilterBuilder()
                .condition("currencySymbol == 'EUR' or currencyName == 'Dollar'")
                .like("name", "B%", false);

        Condition and = fb.toAnd();
        Set keys = countries.keySet(and);
        assertEquals(7, keys.size());

        Condition or = fb.toOr();
        keys = countries.keySet(or);
        assertEquals(92, keys.size());
    }

    // ---- helper methods --------------------------------------------------

    private void loadCountries(String csvFile)
        {
        new CsvToCoherence(csvFile, countries, Country.class).load();
        }
}
