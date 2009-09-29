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

package com.seovic.coherence.lookup;


import com.tangosol.util.aggregator.AbstractAggregator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;


/**
 * An aggregator that creates and returns  a collection of {@link LookupValue}
 * instances by invoking {@link LookupValueExtractor} on each aggeragated
 * cache entry.
 * 
 * @author Aleksandar Seovic  2008.12.19
 */
@SuppressWarnings("unchecked")
public class LookupValuesAggregator<TId, TDesc>
        extends AbstractAggregator
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor. For internal use only.
     */
    public LookupValuesAggregator()
        {
        }

    /**
     * Construct <tt>LookupValuesAggregator</tt> instance.
     *
     * @param extractor  the extractor to use
     */
    public LookupValuesAggregator(LookupValueExtractor<TId, TDesc> extractor)
        {
        super(extractor);
        }


    // ---- AbstractAggregator implementation -------------------------------

    /**
     * Initialize partial or final aggregator.
     *
     * @param isFinal  flag specifying whether this is a final (root) aggregator
     */
    protected void init(boolean isFinal)
        {
        results = new ArrayList<LookupValue<TId, TDesc>>();
        }

    /**
     * Process a single item or the result of a parellel aggregator.
     *
     * @param o        object to process; result of a parallel aggregator if
     *                 <tt>isFinal</tt> is true, or a {@link LookupValue}
     *                 extracted from the aggregated cache entry if
     *                 <tt>isFinal</tt> is false
     * @param isFinal  flag specifying whether this is a final (root) aggregator
     */
    protected void process(Object o, boolean isFinal)
        {
        if (isFinal)
            {
            results.addAll((Collection<? extends LookupValue<TId, TDesc>>) o);
            }
        else
            {
            results.add((LookupValue) o);
            }
        }

    /**
     * Finalizes and returns the aggregation result.
     *
     * @param isFinal  flag specifying whether this is a final (root) aggregator
     */
    protected Object finalizeResult(boolean isFinal)
        {
        return results;
        }

    // ---- data members ----------------------------------------------------

    /**
     * A list that should be used to store results of either parallel or
     * final aggregation.
     */
    private transient List<LookupValue<TId, TDesc>> results;
    }
