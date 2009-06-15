package com.seovic.coherence.loader.target;


import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import com.seovic.coherence.identity.IdentityGenerator;
import com.seovic.coherence.identity.IdentityExtractor;
import com.seovic.coherence.identity.extractor.EntityIdentityExtractor;

import com.seovic.coherence.loader.PropertySetter;
import com.seovic.coherence.loader.Source;
import com.seovic.coherence.loader.properties.BeanWrapperPropertySetter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Constructor;


/**
 * @author ic  2009.06.09
 */
@SuppressWarnings("unchecked")
public class CoherenceCacheTarget extends AbstractBaseTarget {
    public static final int DEFAULT_BATCH_SIZE = 1000;

    private NamedCache        cache;
    private Class             itemClass;
    private IdentityGenerator idGenerator;
    private IdentityExtractor idExtractor;
    private Map               batch;
    private int               batchSize = DEFAULT_BATCH_SIZE;

    private transient Constructor itemCtor;

    public CoherenceCacheTarget(String cacheName, Class itemClass) {
        this(cacheName, itemClass, null, new EntityIdentityExtractor());
    }

    public CoherenceCacheTarget(String cacheName, Class itemClass, IdentityGenerator idGenerator) {
        this(cacheName, itemClass, idGenerator, null);
    }

    public CoherenceCacheTarget(String cacheName, Class itemClass, IdentityExtractor idExtractor) {
        this(cacheName, itemClass, null, idExtractor);
    }

    private CoherenceCacheTarget(String cacheName, Class itemClass, IdentityGenerator idGenerator, IdentityExtractor idExtractor) {
        this.cache       = CacheFactory.getCache(cacheName);
        this.itemClass   = itemClass;
        this.idGenerator = idGenerator;
        this.idExtractor = idExtractor;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * {@inheritDoc}
     */
    protected PropertySetter createDefaultSetter(String propertyName) {
        return new BeanWrapperPropertySetter(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    public void beginImport() {
        batch = new HashMap();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void importItem(Object item) {
        Object id = idGenerator != null
                ? idGenerator.generateIdentity()
                : idExtractor.extractIdentity(item);

        batch.put(id, item);
        if (batch.size() % batchSize == 0) {
            cache.putAll(batch);
            batch.clear();
            System.out.println("Cache size: " + cache.size());
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void endImport() {
        if (!batch.isEmpty()) {
            cache.putAll(batch);
            System.out.println("Cache size: " + cache.size());
        }
    }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames() {
        List<PropertyDescriptor> properties    = getWriteableProperties(itemClass);
        String[]                 propertyNames = new String[properties.size()];

        int i = 0;
        for (PropertyDescriptor pd : properties) {
            propertyNames[i++] = pd.getName();
        }

        return propertyNames;
    }

    /**
     * {@inheritDoc}
     * @param source
     * @param sourceItem
     */
    public Object createTargetInstance(Source source, Object sourceItem) {
        try {
            if (itemCtor == null) {
                itemCtor = itemClass.getConstructor();
            }
            return itemCtor.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
