package com.seovic.coherence.identity;


import com.seovic.coherence.identity.sequence.SequenceGenerator;
import com.seovic.coherence.identity.sequence.Sequence;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for SequenceGenerator.
 *
 * @author  Aleksandar Seovic  2008.11.24
 */
public class SequenceGeneratorTests
    {
    private static NamedCache sequenceCache = CacheFactory.getCache("sequences");

    @Before
    public void before() throws Exception {
        sequenceCache.clear();
    }

    @Test
    public void testIdGeneration() {
        IdentityGeneratorClient igc = new IdentityGeneratorClient(new SequenceGenerator("test"));

        assertEquals(100, igc.generateIdentities(1, 100).size());
        assertEquals(100, igc.generateIdentities(5, 20).size());
        assertEquals(100, igc.generateIdentities(10, 10).size());

        Sequence seq = (Sequence) sequenceCache.get("test");
        assertEquals(300, seq.peek());
    }

    @Test
    public void testIdGenerationWithoutBlockCaching() {
        IdentityGeneratorClient igc = new IdentityGeneratorClient(new SequenceGenerator("test", 1));

        assertEquals(100, igc.generateIdentities(5, 20).size());

        Sequence seq = (Sequence) sequenceCache.get("test");
        assertEquals(100, seq.peek());
    }

    @Test
    public void testIdGenerationWithMultipleClients() {
        IdentityGeneratorClient igc1 = new IdentityGeneratorClient(new SequenceGenerator("test", 10));
        IdentityGeneratorClient igc2 = new IdentityGeneratorClient(new SequenceGenerator("test", 10));

        assertEquals(25, igc1.generateIdentities(5, 5).size());
        assertEquals(25, igc2.generateIdentities(5, 5).size());

        Sequence seq = (Sequence) sequenceCache.get("test");
        assertEquals(60, seq.peek());
    }

}
