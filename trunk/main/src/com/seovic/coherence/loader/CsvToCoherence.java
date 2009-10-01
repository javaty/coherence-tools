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

package com.seovic.coherence.loader;


import com.seovic.coherence.loader.source.CsvSource;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import java.io.Reader;
import java.io.FileReader;

import org.springframework.core.io.Resource;


/**
 * Convenience class that loads data from the CSV file into Coherence cache
 * using default settings.
 *
 * @author Aleksandar Seovic  2009.09.29
 */
public class CsvToCoherence
        extends AbstractDelegatingLoader
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct CsvToCoherence loader instance.
     *
     * @param csvFile    CSV file to read items from
     * @param cache      Coherence cache to import objects into
     * @param itemClass  target item class
     */
    public CsvToCoherence(String csvFile, NamedCache cache, Class itemClass)
        {
        this(getResource(csvFile), cache, itemClass);
        }

    /**
     * Construct CsvToCoherence loader instance.
     *
     * @param csvFile    CSV file to read items from
     * @param cache      Coherence cache to import objects into
     * @param itemClass  target item class
     */
    public CsvToCoherence(Resource csvFile, NamedCache cache, Class itemClass)
        {
        this(createResourceReader(csvFile), cache, itemClass);
        }

    /**
     * Construct CsvToCoherence loader instance.
     *
     * @param csvReader  CSV file reader
     * @param cache      Coherence cache to import objects into
     * @param itemClass  target item class
     */
    public CsvToCoherence(Reader csvReader, NamedCache cache, Class itemClass)
        {
        Source source = new CsvSource(csvReader);
        Target target = new CoherenceCacheTarget(cache, itemClass);
        setLoader(new DefaultLoader(source, target));
        }


    // ---- main method -----------------------------------------------------

    public static void main(String[] args)
            throws Exception
        {
        if (args.length < 3)
            {
            System.out.println("Usage: java com.seovic.coherence.loader.CsvToCoherence <csvFile> <cacheName> <itemClass>");
            System.exit(0);
            }

        FileReader csvReader = new FileReader(args[0]);
        NamedCache cache     = CacheFactory.getCache(args[1]);
        Class      itemClass = Class.forName(args[2]);

        new CsvToCoherence(csvReader, cache, itemClass).load();
        }
    }
