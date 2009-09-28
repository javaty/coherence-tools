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

package com.seovic.coherence.repository;


import com.seovic.lang.Entity;

import com.tangosol.net.NamedCache;

import com.tangosol.util.MapTrigger;
import com.tangosol.util.MapTriggerListener;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;


/**
 * Base class for repository implementations that access Coherence cache.
 *
 * @author Aleksandar Seovic  2008.10.20
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCoherenceRepository<K, V extends Entity<K>>
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct repository instance.
     */
    protected AbstractCoherenceRepository()
        {
        if (!isCacheServiceThread())
            {
            createIndexes();
            registerTriggers();
            }
        }


    // ---- abstract methods ------------------------------------------------

    /**
     * Get the backing cache for this repository.
     *
     * @return the backing cache for this repository
     */
    protected abstract NamedCache getCache();


    // ---- CRUD operations -------------------------------------------------

    /**
     * Return entity from the repository.
     *
     * @param id  entity id
     *
     * @return an entity with the specified id
     */
    public V get(K id)
        {
        return (V) getCache().get(id);
        }

    /**
     * Return all entities from the repository.
     *
     * @return a collection of all entities within the repository
     */
    public Collection<V> getAll()
        {
        return (Collection<V>) getCache().values();
        }

    /**
     * Return a subset of entities from the repository.
     *
     * @param ids  ids of the entities to retrieve
     *
     * @return entities with specified ids
     */
    public Collection<V> getAll(Collection<K> ids)
        {
        return (Collection<V>) getCache().getAll(ids).values();
        }

    /**
     * Save entity into a repository.
     *
     * @param obj  entity to save
     */
    public void save(V obj)
        {
        getCache().putAll(Collections.singletonMap(obj.getId(), obj));
        }

    /**
     * Save multiple entities into a repository.
     *
     * @param objects  entities to save
     */
    public void saveAll(Collection<V> objects)
        {
        Map entries = new HashMap();
        for (V obj : objects)
            {
            entries.put(obj.getId(), obj);
            }
        getCache().putAll(entries);
        }

    /**
     * Save multiple entities into a repository.
     *
     * @param objects  entities to save
     */
    public void saveAll(V... objects)
        {
        saveAll(Arrays.asList(objects));
        }

    /**
     * Remove entity from a repository.
     *
     * @param id  entity id
     */
    public void remove(K id)
        {
        getCache().remove(id);
        }

    /**
     * Remove entity from a repository.
     *
     * @param obj  entity to remove
     */
    public void removeValue(V obj)
        {
        remove(obj.getId());
        }


    // ---- misc cache operations -------------------------------------------

    /**
     * Return <tt>true</tt> if the repository contains entity with a specified
     * identifier.
     *
     * @param id  entity id
     *
     * @return <tt>true</tt> if the repository contains entity with a specified
     *         identifier, <tt>false</tt> otherwise
     */
    public boolean contains(K id)
        {
        return getCache().containsKey(id);
        }

    /**
     * Return <tt>true</tt> if the repository contains specified entity.
     *
     * @param obj  entity to look for
     *
     * @return <tt>true</tt> if the repository contains specified entity,
     *         <tt>false</tt> otherwise
     */
    public boolean containsValue(V obj)
        {
        return contains(obj.getId());
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Create necessary indexes for this repository.
     */
    protected void createIndexes()
        {
        // default empty implementation
        }

    /**
     * Register triggers for this repository.
     */
    protected void registerTriggers()
        {
        // default empty implementation
        }

    /**
     * Register individual map trigger.
     *
     * @param trigger  map trigger to register
     */
    protected void registerTrigger(MapTrigger trigger)
        {
        getCache().addMapListener(new MapTriggerListener(trigger));
        }

    /**
     * Return <tt>true</tt> if invoked on the cache service thread.
     *
     * @return <tt>true</tt> if invoked on the cache service thread,
     *         <tt>false</tt> otherwise
     */
    private static boolean isCacheServiceThread()
        {
        final String tn = Thread.currentThread().getName();
        return tn.startsWith("DistributedCache")
               || tn.startsWith("ReplicatedCache")
               || tn.startsWith("OptimisticCache");
        }
    }
