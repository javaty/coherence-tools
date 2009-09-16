package com.seovic.coherence.loader.source;


import com.seovic.util.extractors.OgnlExtractor;
import com.seovic.util.Extractor;

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
        return new OgnlExtractor(propertyName);
        }
    }
