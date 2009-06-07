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

package com.seovic.coherence.util.filter;


import com.seovic.coherence.util.extractor.PropertyExtractor;

import com.tangosol.util.Filter;
import com.tangosol.util.MapIndex;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.ComparisonFilter;
import com.tangosol.util.filter.IndexAwareFilter;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.HashSet;


/**
 * A {@link Filter} implementation that evaluates to <tt>true</tt> if the
 * extracted property value starts with a specified string.
 * <p/>
 * This filter is somewhat similar to the built-in <tt>LikeFilter</tt>, but
 * is significantly lighter and faster as it only evaluates one special case
 * supported by the <tt>LikeFilter</tt>
 * 
 * @author Aleksandar Seovic  2009.06.07
 */
@SuppressWarnings("unchecked")
public class StartsWithFilter
        extends    ComparisonFilter
        implements IndexAwareFilter
    {
    // ---- data members ----------------------------------------------------

    /**
     * Search string.
     */
    private String m_filter;

    /**
     * Flag specifying if case should be ignored when comparing strings.
     */
    private boolean m_ignoreCase;


    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor. For internal use only.
     */
    public StartsWithFilter()
        {
        }

    /**
     * Construct <tt>StartsWithFilter</tt> instance.
     *
     * @param propertyName  the name of the propery to evaluate
     * @param filter        the search string
     * @param ignoreCase    the flag specifying whether case should be ignored
     *                      when comparing strings
     */
    public StartsWithFilter(String propertyName, String filter, boolean ignoreCase)
        {
        this(new PropertyExtractor(propertyName), filter, ignoreCase);
        }

    /**
     * Construct <tt>StartsWithFilter</tt> instance.
     *
     * @param extractor   the property extractor to use
     * @param filter      the search string
     * @param ignoreCase  the flag specifying whether case should be ignored
     *                    when comparing strings
     */
    public StartsWithFilter(ValueExtractor extractor, String filter, boolean ignoreCase)
        {
        super(extractor, filter);
        m_filter     = filter;
        m_ignoreCase = ignoreCase;
        }


    // ---- ExtractorFilter implementation ----------------------------------

    /**
     * {@inheritDoc}
     */
    protected boolean evaluateExtracted(Object o)
        {
        return isMatch((String) o);
        }


    // ---- IndexAwareFilter implementation ---------------------------------

    /**
     * {@inheritDoc}
     */
    public int calculateEffectiveness(Map mapIndexes, Set setKeys)
        {
        return calculateRangeEffectiveness(mapIndexes, setKeys);
        }

    /**
     * {@inheritDoc}
     */
    public Filter applyIndex(Map mapIndexes, Set setKeys)
        {
        MapIndex index = (MapIndex) mapIndexes.get(getValueExtractor());
        if (index == null)
            {
            // there is no relevant index
            return this;
            }

        Map     candidates     = index.getIndexContents();
        Set     matches        = new HashSet();
        boolean abortIfNoMatch = false;

        if (!m_ignoreCase && index.isOrdered())
            {
            candidates     = ((SortedMap) candidates).tailMap(m_filter);
            abortIfNoMatch = true;
            }

        for (Object entry : candidates.entrySet())
            {
            Map.Entry indexEntry    = (Map.Entry) entry;
            String    propertyValue = (String) indexEntry.getKey();
            if (isMatch(propertyValue))
                {
                matches.addAll((Set) indexEntry.getValue());
                }
            else if (abortIfNoMatch)
                {
                break;
                }
            }

        setKeys.retainAll(matches);
        return null;
        }


    // ---- helper methods --------------------------------------------------

    protected boolean isMatch(String value)
        {
        String filter = m_filter;
        int    len    = filter.length();

        return value.regionMatches(m_ignoreCase, 0, filter, 0, len);
        }
    }
