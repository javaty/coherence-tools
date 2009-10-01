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

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Collection;


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
    public void testTop10ByNameAsc()
        {
        TopAggregator ta = new TopAggregator(new ExpressionExtractor("name"),
                                             null, 10);
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++)
            {
            Collection keys = (Collection) countries.aggregate(
                    AlwaysFilter.INSTANCE, ta);
            }
        long end = System.nanoTime();
        System.out.println(
                "Elapsed: " + (end - start) / 1000000 + "ms; Average: "
                + (end - start) / 10000 + "ns");
        //System.out.println(keys);
        }

    @Test
    public void testTop10ByNameDesc()
        {
        TopAggregator ta = new TopAggregator(new ExpressionExtractor("name"),
                                             new InverseComparator(), 10);
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++)
            {
            Collection keys = (Collection) countries.aggregate(
                    AlwaysFilter.INSTANCE, ta);
            }
        long end = System.nanoTime();
        System.out.println(
                "Elapsed: " + (end - start) / 1000000 + "ms; Average: "
                + (end - start) / 10000 + "ns");
        //System.out.println(keys);
        }


    // ---- helper methods --------------------------------------------------

    private void loadCountries(String csvFile)
        {
        new CsvToCoherence(csvFile, countries, Country.class).load();
        }
    }