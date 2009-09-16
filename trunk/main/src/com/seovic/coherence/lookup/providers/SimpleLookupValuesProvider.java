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

package com.seovic.coherence.lookup.providers;


import com.seovic.coherence.lookup.LookupValuesProvider;
import com.seovic.coherence.lookup.LookupValue;
import com.seovic.coherence.lookup.LookupValueExtractor;
import com.seovic.coherence.lookup.LookupValuesAggregator;

import com.seovic.coherence.util.filter.StartsWithFilter;

import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;

import java.util.Collection;


/**
 * A simple implementation of {@link LookupValuesProvider}.
 *
 * @author Aleksandar Seovic  2009.06.07
 */
@SuppressWarnings("unchecked")
public class SimpleLookupValuesProvider<TId, TDesc>
        implements LookupValuesProvider<TId, TDesc>
    {
    // ---- data members ----------------------------------------------------

    /**
     * InvocableMap that lookup values should be retrieved from.
     */
    private InvocableMap m_map;

    /**
     * Extractor that should be used to extract {@link LookupValue}s from
     * map entries.
     */
    private LookupValueExtractor<TId, TDesc> m_extractor;

    
    // ---- constructors ----------------------------------------------------

    /**
     * Construct <tt>SimpleLookupValuesProvider</tt> instance.
     *
     * @param map        map that lookup values should be retrieved from
     * @param extractor  lookup value extractor to use
     */
    public SimpleLookupValuesProvider(InvocableMap map,
                                      LookupValueExtractor<TId, TDesc> extractor)
        {
        m_map       = map;
        m_extractor = extractor;
        }


    // ---- getters and setters ---------------------------------------------

    public InvocableMap getMap()
        {
        return m_map;
        }

    public LookupValueExtractor<TId, TDesc> getExtractor()
        {
        return m_extractor;
        }

    
    // ---- LookupValuesProvider implementation -----------------------------

    /**
     * {@inheritDoc}
     */
    public Collection<LookupValue<TId, TDesc>> getValues()
        {
        return getValuesInternal(null);
        }

    /**
     * {@inheritDoc}
     */
    public Collection<LookupValue<TId, TDesc>> getValues(String filter,
                                                         boolean ignoreCase)
        {
        return getValuesInternal(instantiateDescriptionFilter(filter, ignoreCase));
        }

    /**
     * {@inheritDoc}
     */
    public Collection<LookupValue<TId, TDesc>> getValues(Filter filter)
        {
        return getValuesInternal(filter);
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Create an instance of a description filter to use.
     *
     * @param filter      description filter
     * @param ignoreCase  flag specifying whether to ignore case during
     *                    comparison
     *
     * @return a description that should be used to narrow down the aggregation
     *         scope
     */
    protected Filter instantiateDescriptionFilter(String filter,
                                                  boolean ignoreCase)
        {
        return new StartsWithFilter(m_extractor.getDescriptionExtractor(),
                                    filter, ignoreCase);
        }

    /**
     * Performs aggregation of lookup values from the underlying cache.
     *
     * @param filter  aggregation filter
     *
     * @return a collection of lookup values 
     */
    protected Collection<LookupValue<TId, TDesc>> getValuesInternal(Filter filter)
        {
        return (Collection<LookupValue<TId, TDesc>>)
                m_map.aggregate(filter,
                                new LookupValuesAggregator<TId, TDesc>(m_extractor));
        }
    }
