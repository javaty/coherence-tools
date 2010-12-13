package com.seovic.coherence.index;


import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import com.oracle.coherence.common.events.Event;
import com.oracle.coherence.common.events.dispatching.EventDispatcher;
import com.oracle.coherence.common.events.lifecycle.NamedCacheStorageRealizedEvent;
import com.oracle.coherence.common.events.processing.EventProcessor;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Aleksandar Seovic  2010.09.09
 */
public class IndexManager implements EventProcessor {
    private static final Log LOG = LogFactory.getLog(IndexManager.class);

    private Map<String, List<IndexDefinition>> m_indexDefs =
            new HashMap<String, List<IndexDefinition>>();

    public void addIndexDefinitions(String cacheName, List<IndexDefinition> indexDefs) {
        m_indexDefs.put(cacheName, indexDefs);
    }

    public void createIndexes(String cacheName) {
        List<IndexDefinition> defs = m_indexDefs.get(cacheName);
        if (defs != null) {
            NamedCache cache = CacheFactory.getCache(cacheName);
            for (IndexDefinition def : defs) {
                LOG.info("Adding index " + def + " to cache [" + cacheName + "]");
                cache.addIndex(def.getExtractor(), def.isSorted(), def.getComparator());
            }
        }
    }

    @Override
    public void process(EventDispatcher eventDispatcher, final Event event) {
        if (event instanceof NamedCacheStorageRealizedEvent)
        {
            ExecutorService exec = Executors.newSingleThreadExecutor();
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    createIndexes(((NamedCacheStorageRealizedEvent) event).getCacheName());
                }
            });
        }
    }
}
