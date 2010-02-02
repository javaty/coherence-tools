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

package com.seovic.coherence.util;


import com.tangosol.net.NamedCache;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.ConverterCollections;

import java.util.Map;
import java.util.Set;
import java.util.Collection;


/**
 * @author Aleksandar Seovic  2010.02.01
*/
public class BackingMap<K, V>
        implements Map<K, V>
    {
    // ---- constructors --------------------------------------------

    public BackingMap(NamedCache cache)
        {
        this(cache.getCacheService().getBackingMapManager().getContext(),
             cache.getCacheName());
        }

    @SuppressWarnings({"unchecked"})
    public BackingMap(BackingMapManagerContext context, String cacheName)
        {
        m_backingMap = ConverterCollections.getMap(
                context.getBackingMap(cacheName),
                context.getKeyFromInternalConverter(),
                context.getKeyToInternalConverter(),
                context.getValueFromInternalConverter(),
                context.getValueToInternalConverter());
        }

    // ---- Map implementation --------------------------------------

    public int size()
        {
        return m_backingMap.size();
        }

    public boolean isEmpty()
        {
        return m_backingMap.isEmpty();
        }

    public boolean containsKey(Object key)
        {
        return m_backingMap.containsKey(key);
        }

    public boolean containsValue(Object value)
        {
        return m_backingMap.containsValue(value);
        }

    public V get(Object key)
        {
        return m_backingMap.get(key);
        }

    public V put(K key, V value)
        {
        return m_backingMap.put(key, value);
        }

    public V remove(Object key)
        {
        return m_backingMap.remove(key);
        }

    public void putAll(Map<? extends K, ? extends V> m)
        {
        m_backingMap.putAll(m);
        }

    public void clear()
        {
        m_backingMap.clear();
        }

    public Set<K> keySet()
        {
        return m_backingMap.keySet();
        }

    public Collection<V> values()
        {
        return m_backingMap.values();
        }

    public Set<Entry<K, V>> entrySet()
        {
        return m_backingMap.entrySet();
        }

    public boolean equals(Object o)
        {
        return m_backingMap.equals(o);
        }

    public int hashCode()
        {
        return m_backingMap.hashCode();
        }


    // ---- data members --------------------------------------------

    private final Map<K, V> m_backingMap;
    }
