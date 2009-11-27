package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.Source;

import com.seovic.core.Defaults;
import com.seovic.core.Updater;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


/**
 * A {@link com.seovic.coherence.loader.Target} implementation that loads
 * objects into database using JPA.
 *
 * @author Ivan Cikic  2009.11.26
 */
public class JpaTarget
        extends AbstractBaseTarget
    {

    // ---- constructors ----------------------------------------------------

    /**
     * Construct JpaTarget instance.
     *
     * @param persistenceUnitName the name of persistence unit
     * @param entityClass         the entity class
     */
    public JpaTarget(String persistenceUnitName, Class entityClass)
        {
        m_persistenceUnitName = persistenceUnitName;
        m_entityClass         = entityClass;
        }


    // ---- Target implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginImport()
        {
        if (m_emf == null)
            {
            m_emf = Persistence.createEntityManagerFactory(m_persistenceUnitName);
            }
        m_em    = m_emf.createEntityManager();
        m_batch = new ArrayList(m_batchSize);
        }

    /**
     * @param item item to import
     */
    @SuppressWarnings({"unchecked"})
    public void importItem(Object item)
        {
        m_batch.add(item);
        if (m_batch.size() % m_batchSize == 0)
            {
            mergeInTransaction(m_em, m_batch);
            m_batch.clear();
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endImport()
        {
        if (!m_batch.isEmpty())
            {
            mergeInTransaction(m_em, m_batch);
            }
        if (m_em != null && m_em.isOpen())
            {
            m_em.close();
            }
        if (m_emf != null)
            {
            m_emf.close();
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Updater createDefaultUpdater(String propertyName)
        {
        return Defaults.createUpdater(propertyName);
        }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames()
        {
        List<PropertyDescriptor> properties    = getWriteableProperties(m_entityClass);
        String[]                 propertyNames = new String[properties.size()];
        int i = 0;
        for (PropertyDescriptor prop : properties)
            {
            propertyNames[i++] = prop.getName();
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
            if (m_entityCtor == null)
                {
                m_entityCtor = m_entityClass.getConstructor();
                }
            return m_entityCtor.newInstance();
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Persist collection of objects.
     *
     * @param em      entity manager used to persist objects
     * @param objects objects to persist
     */
    private void mergeInTransaction(EntityManager em, Collection objects)
        {
        EntityTransaction tx = null;
        try
            {
            tx = em.getTransaction();
            tx.begin();
            for (Object o : objects)
                {
                em.merge(o);
                }
            tx.commit();
            }
        catch (RuntimeException e)
            {
            if (tx != null && tx.isActive())
                {
                tx.rollback();
                }
            throw e;
            }
        }


    // ---- data members ----------------------------------------------------

    /**
     * Default batch size.
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * The batch size.
     */
    private int m_batchSize = DEFAULT_BATCH_SIZE;

    /**
     * Batch of items.
     */
    private transient List m_batch;

    /**
     * Entity manager factory.
     */
    private EntityManagerFactory m_emf;

    /**
     * Entity manger.
     */
    private EntityManager m_em;

    /**
     * The class of entity to load.
     */
    private Class m_entityClass;

    /**
     * Target item constructor.
     */
    private Constructor m_entityCtor;

    /**
     * The name of persistence unit.
     */
    private String m_persistenceUnitName;
    }
