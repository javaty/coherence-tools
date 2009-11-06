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

package com.seovic.batch.steps;


import com.seovic.batch.AbstractStep;
import com.seovic.batch.ExecutionContext;

import com.tangosol.net.CacheFactory;


/**
 * Step implementation that clears Coherence cache.
 *
 * @author Aleksandar Seovic  2009.11.06
 */
public class ClearCacheStep
        extends AbstractStep
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct ClearCacheStep instance.
     *
     * @param stepName   step name
     * @param cacheName  loader to use
     */
    public ClearCacheStep(String stepName, String cacheName)
        {
        super(stepName);
        m_cacheName = cacheName;
        }


    // ---- Step implementation ---------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void execute(ExecutionContext context)
        {
        CacheFactory.getCache(m_cacheName).clear();
        }


    // ---- data members ----------------------------------------------------

    /**
     * The name of the cache to clear.
     */
    private String m_cacheName;
    }