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

import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.filter.AndFilter;

import java.util.Collection;


/**
 * An implementation of {@link LookupValuesProvider} that allows you to limit
 * aggregation scope to a set of dependent values.
 * <p/>
 * Classic scenario where this provider is applicable is for implementation
 * of dependent drop downs, such as car/make/model or country/city selectors,
 * where the value selected in the higer level drop down is used to narrow
 * down the list of values in the dependent one.
 *
 * @author Aleksandar Seovic  2009.06.07
 */
@SuppressWarnings("unchecked")
public class DependentLookupValuesProvider<TId, TDesc>
        extends SimpleLookupValuesProvider<TId, TDesc>
    {
    // ---- data members ----------------------------------------------------

    /**
     * Filter that is used to narrow down the aggregation scope.
     */
    private Filter m_dependencyFilter;

    // ---- constructors ----------------------------------------------------

    /**
     * Construct <tt>SimpleLookupValuesProvider</tt> instance.
     *
     * @param map               map that lookup values should be retrieved from
     * @param extractor         lookup value extractor to use
     * @param dependencyFilter  filter that should be used to narrow down the
     *                          aggregation scope
     */
    public DependentLookupValuesProvider(InvocableMap map,
                                         LookupValueExtractor<TId, TDesc> extractor,
                                         Filter dependencyFilter)
        {
        super(map, extractor);
        m_dependencyFilter = dependencyFilter;
        }


    // ---- helper methods --------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected Collection<LookupValue<TId, TDesc>> getValuesInternal(Filter filter)
        {
        filter = filter == null
                 ? m_dependencyFilter
                 : new AndFilter(m_dependencyFilter, filter);

        return super.getValuesInternal(filter);
        }
    }