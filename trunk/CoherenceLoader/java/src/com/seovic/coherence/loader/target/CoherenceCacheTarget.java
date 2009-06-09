package com.seovic.coherence.loader.target;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import com.seovic.coherence.identity.IdentityGenerator;
import com.seovic.coherence.identity.IdentityExtractor;
import com.seovic.coherence.identity.extractor.EntityIdentityExtractor;

import com.seovic.coherence.loader.PropertyMapper;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author ic  2009.06.09
 */
public class CoherenceCacheTarget extends AbstractBaseTarget {
    public static final int DEFAULT_BATCH_SIZE = 1000;

    private NamedCache        cache;
    private Class             itemClass;
    private IdentityGenerator idGenerator;
    private IdentityExtractor idExtractor;
    private Map               batch;
    private int               batchSize = DEFAULT_BATCH_SIZE;

    private transient List<PropertyDescriptor> writeableProperties;

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

        this.writeableProperties = getWriteableProperties(itemClass);
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void beginImport() {
        batch = new HashMap();
    }

    @SuppressWarnings("unchecked")
    public void importSingle(Object item) {
        Object convertedItem = mapItem(item);
        Object id = idGenerator != null
                ? idGenerator.generateIdentity()
                : idExtractor.extractIdentity(convertedItem);

        batch.put(id, convertedItem);
        if (batch.size() % batchSize == 0) {
            cache.putAll(batch);
            batch.clear();
            System.out.println("Cache size: " + cache.size());
        }
    }

    @SuppressWarnings("unchecked")
    public void endImport() {
        if (!batch.isEmpty()) {
            cache.putAll(batch);
            System.out.println("Cache size: " + cache.size());
        }
    }

    private Object mapItem(Object source) {
       BeanWrapper beanWrapper = new BeanWrapperImpl(itemClass);
       for (PropertyDescriptor property : writeableProperties) {
            String propertyName = property.getName();
            PropertyMapper pm = getPropertyMapper(propertyName);
            beanWrapper.setPropertyValue(propertyName, pm.getValue(source));
        }
        return beanWrapper.getWrappedInstance();
    }
}
