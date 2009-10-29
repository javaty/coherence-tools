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

package com.seovic.coherence.util.aggregator;


import com.seovic.coherence.loader.CsvToCoherence;

import com.seovic.core.extractor.ExpressionExtractor;

import com.seovic.test.objects.Country;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import com.tangosol.util.filter.AlwaysFilter;

import com.tangosol.util.comparator.InverseComparator;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.List;


@SuppressWarnings("unchecked")
public class TopAggregatorTests
    {
    private static final NamedCache countries =
            CacheFactory.getCache("countries");

    @BeforeClass
    public static void beforeClass()
        {
        countries.addIndex(new ExpressionExtractor("name"), false, null);
        }
    
    @Before
    public void clearCache()
            throws Exception
        {
        countries.clear();
        loadCountries("countries.csv");
        }

    @Test
    public void testTop10ByCodeAsc()
        {
        TopAggregator ta      = new TopAggregator(new ExpressionExtractor("code"), null, 10);
        List<String>  results = (List<String>) countries.aggregate(AlwaysFilter.INSTANCE, ta);

        assertEquals(10, results.size());
        String prev = results.get(0);
        for (int i = 1; i < 10; i++)
            {
            assertTrue(prev.compareTo(results.get(i)) <= 0);
            prev = results.get(i);
            }
        }

    @Test
    public void testTop10ByCodeDesc()
        {
        TopAggregator ta      = new TopAggregator(new ExpressionExtractor("code"),
                                                  new InverseComparator(), 10);
        List<String>  results = (List<String>) countries.aggregate(AlwaysFilter.INSTANCE, ta);

        assertEquals(10, results.size());
        String prev = results.get(0);
        for (int i = 1; i < 10; i++)
            {
            assertTrue(prev.compareTo(results.get(i)) >= 0);
            prev = results.get(i);
            }
        }


    // ---- helper methods --------------------------------------------------

    private void loadCountries(String csvFile)
        {
        new CsvToCoherence(csvFile, countries, Country.class).load();
        }
    }