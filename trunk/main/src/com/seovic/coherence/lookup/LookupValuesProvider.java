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


import java.util.Collection;

import com.tangosol.util.Filter;


/**
 * An interface that different lookup value providers have to implement.
 *
 * @author Aleksandar Seovic  2009.06.05
 */
public interface LookupValuesProvider<TId, TDesc>
    {
    /**
     * Return a collection of all lookup values from the underlying store.
     *
     * @return a collection of all lookup values in the underlying store
     */
    Collection<LookupValue<TId, TDesc>> getValues();

    /**
     * Return a collection of all lookup values from the underlying store whose
     * description starts with a specified <tt>filter</tt>.
     *
     * @param filter      description filter
     * @param ignoreCase  flag specifying whether to ignore case during
     *                    comparison
     *
     * @return a collection of all lookup values in the underlying store whose
     * description starts with a specified <tt>filter</tt>.
     */
    Collection<LookupValue<TId, TDesc>> getValues(String filter,
                                                  boolean ignoreCase);

    /**
     * Return a collection of all lookup values from the underlying store that
     * satisfy the specified <tt>condition</tt>.
     *
     * @param condition  condition to evaluate
     *
     * @return a collection of all lookup values in the underlying store that
     *         satisfy the specified <tt>condition</tt>
     */
    Collection<LookupValue<TId, TDesc>> getValues(Filter condition);
    }
