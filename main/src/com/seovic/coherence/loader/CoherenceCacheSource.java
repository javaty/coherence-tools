package com.seovic.coherence.loader;


import com.seovic.core.Extractor;
import com.seovic.core.Defaults;

import com.seovic.loader.Source;
import com.seovic.loader.source.AbstractBaseSource;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

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
     * @param cacheName  the name of the cache to read objects from
     */
    public CoherenceCacheSource(String cacheName)
        {
        m_cacheName = cacheName;
        }

    /**
     * Construct a CoherenceCacheSource instance.
     * <p/>
     * This constructor should only be used when using CoherenceCacheSource
     * in process. In situations where this object might be serialized and
     * used in a remote process (as part of remote batch load job, for example),
     * you should use the constructor that accepts resource name as an argument.
     *
     * @param cache  the cache to read objects from
     */
    public CoherenceCacheSource(NamedCache cache)
        {
        m_cache = cache;
        }

    // ---- Source implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void beginExport()
        {
        if (m_cache == null)
            {
            m_cache = CacheFactory.getCache(m_cacheName);
            }
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
     * The name of the cache to read objects from.
     */
    private String m_cacheName;

    /**
     * The cache to read objects from.
     */
    private transient NamedCache m_cache;
    }
