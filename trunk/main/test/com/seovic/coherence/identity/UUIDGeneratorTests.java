package com.seovic.coherence.identity;


import com.seovic.coherence.identity.uuid.UUIDGenerator;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests for UUIDGenerator.
 * 
 * @author Aleksandar Seovic  2008.11.24
 */
public class UUIDGeneratorTests {

    @Test
    public void testIdGeneration() {
        IdentityGeneratorClient igc = new IdentityGeneratorClient(new UUIDGenerator());

        assertEquals(100, igc.generateIdentities(1, 100).size());
        assertEquals(100, igc.generateIdentities(5, 20).size());
        assertEquals(100, igc.generateIdentities(10, 10).size());
    }
}
