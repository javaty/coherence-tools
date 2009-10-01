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

package com.seovic.coherence.loader;


import org.springframework.core.io.Resource;
import org.springframework.core.io.DefaultResourceLoader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;


/**
 * Base class for delegating loaders.
 *
 * @author Aleksandar Seovic  2009.09.29
 */
public class AbstractDelegatingLoader
        implements Loader
    {
    // ---- getters and setters ---------------------------------------------

    /**
     * Return the loader processing should be delegated to.
     *
     * @return the loader processing should be delegated to
     */
    public Loader getLoader()
        {
        return m_loader;
        }

    /**
     * Set the loader processing should be delegated to.
     *
     * @param loader  the loader processing should be delegated to
     */
    public void setLoader(Loader loader)
        {
        m_loader = loader;
        }


    // ---- Loader implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void load()
        {
        m_loader.load();
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Return a Resource represented by the specified location.
     *
     * @param location  resource location
     *
     * @return a resource
     */
    protected static Resource getResource(String location)
        {
        return new DefaultResourceLoader().getResource(location);
        }

    /**
     * Create a reader for the specified resource.
     *
     * @param resource  resource to create reader for
     *
     * @return reader for the specified resource
     */
    protected static Reader createResourceReader(Resource resource)
        {
        try
            {
            return new InputStreamReader(resource.getInputStream());
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- data members ----------------------------------------------------

    /**
     * The loader processing should be delegated to.
     */
    private Loader m_loader;
    }
