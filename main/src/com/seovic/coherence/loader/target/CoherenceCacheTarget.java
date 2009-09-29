package com.seovic.coherence.loader.target;


import com.seovic.coherence.identity.IdentityExtractor;
import com.seovic.coherence.identity.IdentityGenerator;

import com.seovic.coherence.identity.extractor.EntityIdentityExtractor;

import com.seovic.coherence.loader.Source;

import com.seovic.core.Updater;
import com.seovic.core.Defaults;

import com.tangosol.net.NamedCache;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ic  2009.06.09
 */
@SuppressWarnings("unchecked")
public class CoherenceCacheTarget
        extends AbstractBaseTarget
    {
    public static final int DEFAULT_BATCH_SIZE = 1000;

    private NamedCache cache;

    private Class itemClass;

    private IdentityGenerator idGenerator;

    private IdentityExtractor idExtractor;

    private Map batch;

    private int batchSize = DEFAULT_BATCH_SIZE;

    private transient Constructor itemCtor;

    public CoherenceCacheTarget(NamedCache cache, Class itemClass)
        {
        this(cache, itemClass, null, new EntityIdentityExtractor());
        }

    public CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                IdentityGenerator idGenerator)
        {
        this(cache, itemClass, idGenerator, null);
        }

    public CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                IdentityExtractor idExtractor)
        {
        this(cache, itemClass, null, idExtractor);
        }

    private CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                 IdentityGenerator idGenerator,
                                 IdentityExtractor idExtractor)
        {
        this.cache = cache;
        this.itemClass = itemClass;
        this.idGenerator = idGenerator;
        this.idExtractor = idExtractor;
        }

    public void setBatchSize(int batchSize)
        {
        this.batchSize = batchSize;
        }

    /**
     * {@inheritDoc}
     */
    protected Updater createDefaultUpdater(String propertyName)
        {
        return Defaults.createUpdater(propertyName);
        }

    /**
     * {@inheritDoc}
     */
    public void beginImport()
        {
        batch = new HashMap();
        }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void importItem(Object item)
        {
        Object id = idGenerator != null
                    ? idGenerator.generateIdentity()
                    : idExtractor.extractIdentity(item);

        batch.put(id, item);
        if (batch.size() % batchSize == 0)
            {
            cache.putAll(batch);
            batch.clear();
            System.out.println("Cache size: " + cache.size());
            }
        }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void endImport()
        {
        if (!batch.isEmpty())
            {
            cache.putAll(batch);
            System.out.println("Cache size: " + cache.size());
            }
        }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames()
        {
        List<PropertyDescriptor> properties = getWriteableProperties(itemClass);
        String[] propertyNames = new String[properties.size()];

        int i = 0;
        for (PropertyDescriptor pd : properties)
            {
            propertyNames[i++] = pd.getName();
            }

        return propertyNames;
        }

    /**
     * {@inheritDoc}
     *
     * @param source
     * @param sourceItem
     */
    public Object createTargetInstance(Source source, Object sourceItem)
        {
        try
            {
            if (itemCtor == null)
                {
                itemCtor = itemClass.getConstructor();
                }
            return itemCtor.newInstance();
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }
    }
