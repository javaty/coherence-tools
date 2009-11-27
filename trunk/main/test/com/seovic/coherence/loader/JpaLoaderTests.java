package com.seovic.coherence.loader;


import com.seovic.coherence.loader.source.CoherenceCacheSource;
import com.seovic.coherence.loader.source.JpaSource;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;
import com.seovic.coherence.loader.target.JpaTarget;
import com.seovic.test.objects.Country;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests for JPA based {@link Source}/{@link Target} implementations.
 *
 * @author Ivan Cikic  2009.11.27
 */
public class JpaLoaderTests
        extends LoaderTests
    {
    private static EntityManagerFactory s_emf =
            Persistence.createEntityManagerFactory("coherence-tools");

    @Before
    public void setUp()
        {
        doInTransaction(new Callback()
        {
        public void execute(EntityManager em)
            {
            em.createQuery("delete from Country").executeUpdate();
            }
        });
        }

    @Test
    public void testJpaSource_singleBatch()
        {
        populate(createCountries());
        Source source = new JpaSource("coherence-tools", Country.class);
        Target target = new CoherenceCacheTarget(countries, Country.class);
        Loader l = new DefaultLoader(source, target);
        l.load();

        assertEquals(3, countries.size());
        assertNotNull(countries.get("SRB"));
        assertNotNull(countries.get("SGP"));
        assertNotNull(countries.get("CHL"));

        Country result = (Country) countries.get("CHL");
        assertEquals("Chile", result.getName());
        assertEquals("+56", result.getTelephonePrefix());
        }

    @Test
    public void testJpaSource_multiBatch()
        {
        populate(createCountries());
        JpaSource source = new JpaSource("coherence-tools", Country.class);
        source.setBatchSize(2);
        Target target = new CoherenceCacheTarget(countries, Country.class);
        Loader l = new DefaultLoader(source, target);
        l.load();

        assertEquals(3, countries.size());
        assertNotNull(countries.get("SRB"));
        assertNotNull(countries.get("SGP"));
        assertNotNull(countries.get("CHL"));

        Country result = (Country) countries.get("CHL");
        assertEquals("Chile", result.getName());
        assertEquals("+56", result.getTelephonePrefix());
        }


    @Test
    public void testJpaTarget()
        {
        prepareCache();
        Source source = new CoherenceCacheSource(countries);
        Target target = new JpaTarget("coherence-tools", Country.class);
        Loader l = new DefaultLoader(source, target);
        l.load();

        EntityManager em = s_emf.createEntityManager();
        try
            {
            assertRowCount(em);
            assertRows(em);
            }
        finally
            {
            em.close();
            }
        }

    protected static Collection<Country> createCountries()
        {
        List<Country> countries = new ArrayList<Country>();
        countries.add(createCountry(
                "SRB,Serbia,Republic of Serbia,Belgrade,RSD,Dinar,+381,.rs and .yu"));
        countries.add(createCountry(
                "SGP,Singapore,Republic of Singapore,Singapore,SGD,Dollar,+65,.sg"));
        countries.add(createCountry(
                "CHL,Chile,Republic of Chile,Santiago (administrative/judical) and Valparaiso (legislative),CLP,Peso,+56,.cl"));
        return countries;
        }

    protected static void populate(Collection<?> objects)
        {
        EntityManagerFactory emf = s_emf;
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try
            {
            tx.begin();
            for (Object o : objects)
                {
                em.persist(o);
                }
            tx.commit();
            }
        catch (Exception e)
            {
            if (tx != null && tx.isActive())
                {
                tx.rollback();
                }
            }
        finally
            {
            em.close();
            }
        }

    private static void assertRowCount(EntityManager em)
        {
        int result = ((Number) em.createQuery("select count(c) from Country c")
                .getSingleResult()).intValue();
        assertEquals(3, result);
        }

    private static void assertRows(EntityManager em)
        {
        assertNotNull(em.find(Country.class, "SRB"));
        assertNotNull(em.find(Country.class, "SGP"));
        Country result = em.find(Country.class, "CHL");
        assertNotNull(result);
        assertEquals("Chile", result.getName());
        assertEquals("+56", result.getTelephonePrefix());
        }

    private static void doInTransaction(Callback callback)
        {
        EntityManager em = s_emf.createEntityManager();
        EntityTransaction t = null;
        try
            {
            t = em.getTransaction();
            t.begin();
            callback.execute(em);
            t.commit();
            }
        catch (RuntimeException e)
            {
            if (t != null && t.isActive())
                {
                t.rollback();
                }
            throw e;
            }
        finally
            {
            em.close();
            }
        }

    private static interface Callback
        {
        void execute(EntityManager em);
        }
    }
