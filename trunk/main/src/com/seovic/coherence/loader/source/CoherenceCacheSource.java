package com.seovic.coherence.loader.source;


import com.seovic.core.Extractor;
import com.seovic.core.Defaults;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.util.Iterator;


/**
 * @author ic  2009.06.09
 */
public class CoherenceCacheSource
        extends AbstractBaseSource
    {
    private NamedCache cache;

    public CoherenceCacheSource(String cacheName)
        {
        this.cache = CacheFactory.getCache(cacheName);
        }

    public Iterator iterator()
        {
        return cache.values().iterator();
        }

    protected Extractor createDefaultExtractor(String propertyName)
        {
        return Defaults.createExtractor(propertyName);
        }
    }
