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

package com.seovic.coherence.util;


import com.seovic.core.Entity;
import com.seovic.core.Updater;

import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.processor.UpdaterProcessor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


/**
 * Base class for repository implementations that access Coherence cache.
 *
 * @author Aleksandar Seovic  2008.10.20
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCoherenceRepository<K, V extends Entity<K>>
    {
    public abstract NamedCache getCache();

    public V get(K key)
        {
        return (V) getCache().get(key);
        }

    public Collection<V> getAll(Collection<K> keys)
        {
        return getCache().getAll(keys).values();
        }

    public void save(V value)
        {
        getCache().putAll(Collections.singletonMap(value.getId(), value));
        }

    public void update(K key, Updater updater, Object value) {
        getCache().invoke(key, new UpdaterProcessor(updater, value));
    }

    public void saveAll(Collection<V> values)
        {
        Map batch = new HashMap(values.size());
        for (V value : values)
            {
            batch.put(value.getId(), value);
            }

        getCache().putAll(batch);
        }

    // ---- helper methods --------------------------------------------------

    protected BackingMap<K, V> getBackingMap()
        {
        return new BackingMap<K,V>(getCache());
        }

    protected V queryForSingleValue(Filter filter)
        {
        Set<Map.Entry<K,V>> entries = getCache().entrySet(filter);

        return entries.size() == 0
               ? null
               : entries.iterator().next().getValue();
        }

    protected Collection<V> queryForValues(Filter filter)
        {
        Set<Map.Entry<K,V>> entries = getCache().entrySet(filter);
        return extractValues(entries);
        }

    protected Collection<V> queryForValues(Filter filter, Comparator comparator)
        {
        Set<Map.Entry<K,V>> entries = getCache().entrySet(filter, comparator);
        return extractValues(entries);
        }

    private Collection<V> extractValues(Set<Map.Entry<K, V>> entries)
        {
        List<V> values = new ArrayList<V>(entries.size());
        for (Map.Entry<K, V> entry : entries)
            {
            values.add(entry.getValue());
            }
        return values;
        }
    }
