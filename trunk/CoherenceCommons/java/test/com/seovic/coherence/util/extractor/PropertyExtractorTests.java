/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.coherence.util.extractor;


import com.tangosol.util.ValueExtractor;
import com.tangosol.util.WrapperException;
import com.tangosol.util.SimpleMapEntry;
import com.tangosol.util.InvocableMapHelper;
import com.tangosol.util.Binary;
import com.tangosol.util.ExternalizableHelper;

import com.tangosol.util.extractor.KeyExtractor;

import com.tangosol.io.DefaultSerializer;

import com.tangosol.io.pof.SimplePofContext;
import com.tangosol.io.pof.PortableObjectSerializer;

import com.seovic.coherence.test.domain.Country;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Map;


/**
 * Tests for PropertyExtractor.
 *
 * @author Aleksandar Seovic  2009.06.05
 */
public class PropertyExtractorTests
    {
    // ---- Tests -----------------------------------------------------------
    
    @Test
    public void testExtraction()
        {
        Country serbia = new Country("SRB", "Serbia");
        ValueExtractor codeEx = new PropertyExtractor("code");
        ValueExtractor nameEx = new PropertyExtractor("name");

        assertEquals("SRB", codeEx.extract(serbia));
        assertEquals("Serbia", nameEx.extract(serbia));
        }

    @Test
    public void testValueExtractionFromEntry()
        {
        Map.Entry entry = new SimpleMapEntry(null, new Country("SRB", "Serbia"));

        PropertyExtractor codeEx = new PropertyExtractor("code");
        PropertyExtractor nameEx = new PropertyExtractor("name", PropertyExtractor.VALUE);

        assertEquals("SRB",    InvocableMapHelper.extractFromEntry(codeEx, entry));
        assertEquals("Serbia", InvocableMapHelper.extractFromEntry(nameEx, entry));
        }

    @Test
    public void testKeyExtractionFromEntry()
        {
        Map.Entry entry = new SimpleMapEntry(new Country("SRB", "Serbia"), null);

        ValueExtractor    codeEx = new KeyExtractor(new PropertyExtractor("code"));
        PropertyExtractor nameEx = new PropertyExtractor("name", PropertyExtractor.KEY);

        assertEquals("SRB",    InvocableMapHelper.extractFromEntry(codeEx, entry));
        assertEquals("Serbia", InvocableMapHelper.extractFromEntry(nameEx, entry));
        }

    @Test
    public void testNullTarget()
        {
        ValueExtractor ex = new PropertyExtractor("code");
        assertNull(ex.extract(null));
        }

    @Test(expected = RuntimeException.class)
    public void testMissingProperty()
        {
        Country serbia = new Country("SRB", "Serbia");
        ValueExtractor ex = new PropertyExtractor("xyz");

        ex.extract(serbia);
        }

    @Test
    public void testDefaultSerialization()
        {
        Object original = new PropertyExtractor("xyz");
        Binary bin      = ExternalizableHelper.toBinary(original, new DefaultSerializer());
        Object copy     = ExternalizableHelper.fromBinary(bin, new DefaultSerializer());

        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        assertEquals(original.toString(), copy.toString());
        }

    @Test
    public void testPofSerialization()
        {
        SimplePofContext ctx = new SimplePofContext();
        ctx.registerUserType(1, PropertyExtractor.class, new PortableObjectSerializer(1));

        Object original = new PropertyExtractor("xyz");
        Binary bin      = ExternalizableHelper.toBinary(original, ctx);
        Object copy     = ExternalizableHelper.fromBinary(bin, ctx);

        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        assertEquals(original.toString(), copy.toString());
        }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testEquals()
        {
        Object o = new PropertyExtractor("xyz");

        assertTrue(o.equals(o));
        assertFalse(o.equals(null));
        assertFalse(o.equals("invalid class"));
        assertFalse(o.equals(new PropertyExtractor("abc")));
        }
    }