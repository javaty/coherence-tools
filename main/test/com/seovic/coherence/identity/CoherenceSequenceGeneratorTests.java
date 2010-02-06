package com.seovic.coherence.identity;


import com.seovic.identity.IdentityGeneratorClient;
import com.seovic.identity.Sequence;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for {@link CoherenceSequenceGenerator}.
 *
 * @author Aleksandar Seovic  2008.11.24
 */
public class CoherenceSequenceGeneratorTests
    {
    private static NamedCache sequenceCache =
            CacheFactory.getCache(CoherenceSequenceGenerator.CACHE_NAME);

    @Before
    public void before()
            throws Exception
        {
        sequenceCache.clear();
        }

    @Test
    public void testIdGeneration()
        {
        IdentityGeneratorClient igc = new IdentityGeneratorClient(
                new CoherenceSequenceGenerator("test", 20));

        assertEquals(100, igc.generateIdentities(1, 100).size());
        assertEquals(100, igc.generateIdentities(5, 20).size());
        assertEquals(100, igc.generateIdentities(10, 10).size());

        Sequence seq = (Sequence) sequenceCache.get("test");
        assertEquals("test", seq.getName());
        assertEquals(300, seq.getLast());
        }

    @Test
    public void testIdGenerationWithoutBlockCaching()
        {
        IdentityGeneratorClient igc = new IdentityGeneratorClient(
                new CoherenceSequenceGenerator("test", 1));

        assertEquals(100, igc.generateIdentities(5, 20).size());

        Sequence seq = (Sequence) sequenceCache.get("test");
        assertEquals("test", seq.getName());
        assertEquals(100, seq.getLast());
        }

    @Test
    public void testIdGenerationWithMultipleClients()
        {
        IdentityGeneratorClient igc1 = new IdentityGeneratorClient(
                new CoherenceSequenceGenerator("test", 10));
        IdentityGeneratorClient igc2 = new IdentityGeneratorClient(
                new CoherenceSequenceGenerator("test", 10));

        assertEquals(25, igc1.generateIdentities(5, 5).size());
        assertEquals(25, igc2.generateIdentities(5, 5).size());

        Sequence seq = (Sequence) sequenceCache.get("test");
        assertEquals("test", seq.getName());
        assertEquals(60, seq.getLast());
        }

    }
