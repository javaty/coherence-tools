package com.seovic.coherence.loader.source;


import com.seovic.core.Extractor;
import com.seovic.core.Defaults;

import com.seovic.coherence.loader.Source;

import com.tangosol.net.NamedCache;

import java.util.Iterator;


/**
 * A {@link Source} implementation that reads items to load from a Coherence
 * cache.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public class CoherenceCacheSource
        extends AbstractBaseSource
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct a CoherenceCacheSource instance.
     *
     * @param cache  cache to read objects from
     */
    public CoherenceCacheSource(NamedCache cache)
        {
        m_cache = cache;
        }


    // ---- Iterable implementation -----------------------------------------

    /**
     * Return an iterator over this source.
     *
     * @return a source iterator
     */
    public Iterator iterator()
        {
        return m_cache.values().iterator();
        }


    // ---- AbstractBaseSource implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Extractor createDefaultExtractor(String propertyName)
        {
        return Defaults.createExtractor(propertyName);
        }


    // ---- data members ----------------------------------------------------

    /**
     * Cache to read objects from.
     */
    private NamedCache m_cache;
    }
