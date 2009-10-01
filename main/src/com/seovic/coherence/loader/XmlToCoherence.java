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


import com.seovic.coherence.loader.source.XmlSource;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import java.io.Reader;
import java.io.FileReader;

import org.springframework.core.io.Resource;


/**
 * Convenience class that loads data from the XML file into Coherence cache
 * using default settings.
 *
 * @author Aleksandar Seovic  2009.09.29
 */
public class XmlToCoherence
        extends AbstractDelegatingLoader
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct XmlToCoherence loader instance.
     *
     * @param xmlFile    XML file to read items from
     * @param cache      Coherence cache to import objects into
     * @param itemClass  target item class
     */
    public XmlToCoherence(String xmlFile, NamedCache cache, Class itemClass)
        {
        this(getResource(xmlFile), cache, itemClass);
        }

    /**
     * Construct XmlToCoherence loader instance.
     *
     * @param xmlFile    XML file to read items from
     * @param cache      Coherence cache to import objects into
     * @param itemClass  target item class
     */
    public XmlToCoherence(Resource xmlFile, NamedCache cache, Class itemClass)
        {
        this(createResourceReader(xmlFile), cache, itemClass);
        }

    /**
     * Construct XmlToCoherence loader instance.
     *
     * @param xmlReader  XML file reader
     * @param cache      Coherence cache to import objects into
     * @param itemClass  target item class
     */
    public XmlToCoherence(Reader xmlReader, NamedCache cache, Class itemClass)
        {
        Source source = new XmlSource(xmlReader);
        Target target = new CoherenceCacheTarget(cache, itemClass);
        setLoader(new DefaultLoader(source, target));
        }


    // ---- main method -----------------------------------------------------

    public static void main(String[] args)
            throws Exception
        {
        if (args.length < 3)
            {
            System.out.println("Usage: java com.seovic.coherence.loader.XmlToCoherence <xmlFile> <cacheName> <itemClass>");
            System.exit(0);
            }

        FileReader xmlReader = new FileReader(args[0]);
        NamedCache cache     = CacheFactory.getCache(args[1]);
        Class      itemClass = Class.forName(args[2]);

        new XmlToCoherence(xmlReader, cache, itemClass).load();
        }
    }