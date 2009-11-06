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

import com.seovic.coherence.loader.Loader;


/**
 * Step implementation that uses {@link Loader} to import data from one data
 * source into another.
 *
 * @author Aleksandar Seovic  2009.11.06
 */
public class LoaderStep
        extends AbstractStep
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct LoaderStep instance.
     *
     * @param name    step name
     * @param loader  loader to use
     */
    public LoaderStep(String name, Loader loader)
        {
        super(name);
        m_loader = loader;
        }


    // ---- Step implementation ---------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void execute(ExecutionContext context)
        {
        m_loader.load();
        }


    // ---- data members ----------------------------------------------------

    /**
     * Loader to use.
     */
    private Loader m_loader;
    }
