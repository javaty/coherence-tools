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


import com.tangosol.net.cache.CacheStore;

import java.util.Map;
import java.util.Collection;


/**
 * @author Aleksandar Seovic  2010.01.22
 */
public class ConfigurableCacheStore 
        extends ConfigurableCacheLoader
        implements CacheStore
    {
    // ---- data members ----------------------------------------------------

    private CacheStore m_store;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct <tt>ConfigurableBackingMapListener</tt> instance.
     *
     * @param cacheName  name of the cache to set up listener for
     */
    public ConfigurableCacheStore(String cacheName)
        {
        super(cacheName);
        m_store = (CacheStore) m_loader;
        }


    // ---- CacheStore implementation ---------------------------------------

    public void store(Object o, Object o1)
        {
        m_store.store(o, o1);
        }

    public void storeAll(Map map)
        {
        m_store.storeAll(map);
        }

    public void erase(Object o)
        {
        m_store.erase(o);
        }

    public void eraseAll(Collection collection)
        {
        m_store.eraseAll(collection);
        }
    }
