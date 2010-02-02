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

package com.seovic.coherence.util.persistence;


import com.tangosol.net.cache.CacheLoader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.Collection;


/**
 * @author Aleksandar Seovic  2010.01.22
 */
public class ConfigurableCacheLoader
        implements CacheLoader
    {
    // ---- configuration context -------------------------------------------

    private static final ApplicationContext s_ctx =
            new ClassPathXmlApplicationContext(
                    System.getProperty("persistence.context",
                                       "persistence-context.xml"));


    // ---- data members ----------------------------------------------------

    protected CacheLoader m_loader;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct <tt>ConfigurableCacheLoader</tt> instance.
     *
     * @param cacheName  name of the cache to set up loader for
     */
    public ConfigurableCacheLoader(String cacheName)
        {
        m_loader = (CacheLoader) s_ctx.getBean(cacheName);
        }


    // ---- CacheLoader implementation ---------------------------------------

    public Object load(Object o)
        {
        return m_loader.load(o);
        }

    public Map loadAll(Collection collection)
        {
        return m_loader.loadAll(collection);
        }
    }