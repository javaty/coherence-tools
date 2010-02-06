package com.seovic.coherence.identity;


import com.seovic.identity.IdentityGeneratorClient;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests for {@link CoherenceUuidGenerator}.
 *
 * @author Aleksandar Seovic  2008.11.24
 */
public class CoherenceUuidGeneratorTests
    {
    @Test
    public void testIdGeneration()
        {
        IdentityGeneratorClient igc = new IdentityGeneratorClient(
                new CoherenceUuidGenerator());

        assertEquals(100, igc.generateIdentities(1, 100).size());
        assertEquals(100, igc.generateIdentities(5, 20).size());
        assertEquals(100, igc.generateIdentities(10, 10).size());
        }
    }
