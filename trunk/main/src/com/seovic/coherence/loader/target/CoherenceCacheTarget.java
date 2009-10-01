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

import java.beans.PropertyDescriptor;

import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A {@link Target} implementation that load objects into Coherence cache.
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
     * @param cache      cache to load objects into
     * @param itemClass  target item class
     */
    public CoherenceCacheTarget(NamedCache cache, Class itemClass)
        {
        this(cache, itemClass, null, new EntityIdentityExtractor());
        }

    /**
     * Construct CoherenceCacheTarget instance.
     *
     * @param cache        cache to load objects into
     * @param itemClass    target item class
     * @param idGenerator  identity generator to use to determine key
     */
    public CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                IdentityGenerator idGenerator)
        {
        this(cache, itemClass, idGenerator, null);
        }

    /**
     * Construct CoherenceCacheTarget instance.
     *
     * @param cache        cache to load objects into
     * @param itemClass    target item class
     * @param idExtractor  identity extractor to use to determine key
     */
    public CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                IdentityExtractor idExtractor)
        {
        this(cache, itemClass, null, idExtractor);
        }

    /**
     * Initializes CoherenceCacheTarget instance.
     *
     * @param cache        cache to load objects into
     * @param itemClass    target item class
     * @param idGenerator  identity generator to use to determine key
     * @param idExtractor  identity extractor to use to determine key
     */
    private CoherenceCacheTarget(NamedCache cache, Class itemClass,
                                 IdentityGenerator idGenerator,
                                 IdentityExtractor idExtractor)
        {
        this.cache = cache;
        this.itemClass = itemClass;
        this.idGenerator = idGenerator;
        this.idExtractor = idExtractor;
        }


    // ---- public API ------------------------------------------------------

    /**
     * Set batch size.
     *
     * @param batchSize  batch size
     */
    public void setBatchSize(int batchSize)
        {
        this.batchSize = batchSize;
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


    // ---- data members ----------------------------------------------------

    /**
     * Default batch size.
     */
    public static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * Cache to load objects into.
     */
    private NamedCache cache;

    /**
     * The class of objects to load.
     */
    private Class itemClass;

    /**
     * Identity generator.
     */
    private IdentityGenerator idGenerator;

    /**
     * Identity extractor.
     */
    private IdentityExtractor idExtractor;

    /**
     * Batch of items.
     */
    private Map batch;

    /**
     * Batch size.
     */
    private int batchSize = DEFAULT_BATCH_SIZE;

    /**
     * Target item constructor.
     */
    private Constructor itemCtor;
    }
