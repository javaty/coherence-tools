package com.seovic.coherence.loader.source;

import com.seovic.coherence.loader.Source;
import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import java.util.Iterator;

/**
 * @author ic  2009.06.09
 */
public class CoherenceCacheSource implements Source {
    private NamedCache cache;

    public CoherenceCacheSource(String cacheName) {
        this.cache = CacheFactory.getCache(cacheName);
    }

    public Iterator iterator() {
        return cache.values().iterator();
    }
}
