package com.seovic.coherence.loader.target;


import com.seovic.coherence.identity.IdentityExtractor;
import com.seovic.coherence.identity.IdentityGenerator;

import com.seovic.coherence.identity.extractor.EntityIdentityExtractor;

import com.seovic.coherence.loader.Source;
import com.seovic.coherence.loader.Target;

import com.seovic.core.Updater;
import com.seovic.core.Defaults;
import com.seovic.core.Entity;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A {@link Target} implementation that loads objects into Coherence cache.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
@SuppressWarnings("unchecked")
public class CoherenceCacheTarget
        extends AbstractBaseTarget
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct CoherenceCacheTarget instance.
     * <p/>
     * This constructor assumes that target class implements {@link Entity}
     * interface and will use {@link EntityIdentityExtractor} to determine
     * cache key.
     *
     * @param cacheName  the name of the cache to load objects into
     * @param itemClass  target item class
     */
    public CoherenceCacheTarget(String cacheName, Class itemClass)
        {
        init(cacheName, null, itemClass, null, new EntityIdentityExtractor());
        }

    /**
     * Construct CoherenceCacheTarget instance.
     *
     * @param cacheName    the name of the cache to load objects into
     * @param itemClass    target item class
     * @param idGenerator  identity generator to use to determine key
     */
    public CoherenceCacheTarget(String cacheName, Class itemClass,
                                IdentityGenerator idGenerator)
        {
        init(cacheName, null, itemClass, idGenerator, null);
        }

    /**
     * Construct CoherenceCacheTarget instance.
     *
     * @param cacheName    the name of the cache to load objects into
     * @param itemClass    target item class
     * @param idExtractor  identity extractor to use to determine key
     */
    public CoherenceCacheTarget(String cacheName, Class itemClass,
                                IdentityExtractor idExtractor)
        {
        init(cacheName, null, itemClass, null, idExtractor);
        }

    /**
     * Construct CoherenceCacheTarget instance.
     * <p/>
     * This constructor assumes that target class implements {@link Entity}
     * interface and will use {@link EntityIdentityExtractor} to determine
     * cache key.
     * <p/>
     * This constructor should only be used when using this target in process.
     * In situations where this object might be serialized and used in a
     * remote process (as part of remote batch load job, for example), you
     * should use the constructor that accepts cache name as an argument
     * instead.
     *
     * @param cache      the cache to load objects into
     * @param itemClass  target item class
     */
    public CoherenceCacheTarget(NamedCache cache, Class itemClass)
        {
        init(null, cache, itemClass, null, new EntityIdentityExtractor());
        }

    /**
     * Construct CoherenceCacheTarget instance.
     * <p/>
     * This constructor should only be used when using this target in process.
     * In situations where this object might be serialized and used in a
     * remote process (as part of remote batch load job, for example), you
     * should use the constructor that accepts cache name as an argument
     * instead.
     *
     * @param cache        the cache to load objects into
     * @param itemClass    target item class
     * @param idGenerator  identity generator to use to determine key
     */
    public CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                IdentityGenerator idGenerator)
        {
        init(null, cache, itemClass, idGenerator, null);
        }

    /**
     * Construct CoherenceCacheTarget instance.
     * <p/>
     * This constructor should only be used when using this target in process.
     * In situations where this object might be serialized and used in a
     * remote process (as part of remote batch load job, for example), you
     * should use the constructor that accepts cache name as an argument
     * instead.
     *
     * @param cache        the cache to load objects into
     * @param itemClass    target item class
     * @param idExtractor  identity extractor to use to determine key
     */
    public CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                IdentityExtractor idExtractor)
        {
        init(null, cache, itemClass, null, idExtractor);
        }

    /**
     * Initializes CoherenceCacheTarget instance.
     *
     * @param cacheName    the name of the cache to load objects into
     * @param cache        the cache to load objects into
     * @param itemClass    target item class
     * @param idGenerator  identity generator to use to determine key
     * @param idExtractor  identity extractor to use to determine key
     */
    private void init(String cacheName, NamedCache cache, Class itemClass,
                      IdentityGenerator idGenerator, IdentityExtractor idExtractor)
        {
        m_cacheName   = cacheName;
        m_cache       = cache;
        m_itemClass   = itemClass;
        m_idGenerator = idGenerator;
        m_idExtractor = idExtractor;
        }

    // ---- public API ------------------------------------------------------

    /**
     * Set batch size.
     *
     * @param batchSize  batch size
     */
    public void setBatchSize(int batchSize)
        {
        m_batchSize = batchSize;
        }


    // ---- AbstractBaseTarget implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Updater createDefaultUpdater(String propertyName)
        {
        return Defaults.createUpdater(propertyName);
        }


    // ---- Source implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void beginImport()
        {
        if (m_cache == null)
            {
            m_cache = CacheFactory.getCache(m_cacheName);
            }
        m_batch = new HashMap(m_batchSize);
        }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void importItem(Object item)
        {
        Object id = m_idGenerator != null
                    ? m_idGenerator.generateIdentity()
                    : m_idExtractor.extractIdentity(item);

        m_batch.put(id, item);
        if (m_batch.size() % m_batchSize == 0)
            {
            m_cache.putAll(m_batch);
            m_batch.clear();
            }
        }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void endImport()
        {
        if (!m_batch.isEmpty())
            {
            m_cache.putAll(m_batch);
            }
        }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames()
        {
        List<PropertyDescriptor> properties = getWriteableProperties(m_itemClass);
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
     */
    public Object createTargetInstance(Source source, Object sourceItem)
        {
        try
            {
            if (m_itemCtor == null)
                {
                m_itemCtor = m_itemClass.getConstructor();
                }
            return m_itemCtor.newInstance();
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- data members ----------------------------------------------------

    /**
     * Default batch size.
     */
    public static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * The name of the cache to load objects into.
     */
    private String m_cacheName;

    /**
     * Cache to load objects into.
     */
    private transient NamedCache m_cache;

    /**
     * The class of objects to load.
     */
    private Class m_itemClass;

    /**
     * Identity generator.
     */
    private IdentityGenerator m_idGenerator;

    /**
     * Identity extractor.
     */
    private IdentityExtractor m_idExtractor;

    /**
     * Batch of items.
     */
    private transient Map m_batch;

    /**
     * Batch size.
     */
    private int m_batchSize = DEFAULT_BATCH_SIZE;

    /**
     * Target item constructor.
     */
    private transient Constructor m_itemCtor;
    }
