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
import com.tangosol.util.extractor.KeyExtractor;

import com.seovic.coherence.test.domain.Country;

import org.junit.Test;

import static org.junit.Assert.*;
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

    @Test(expected = WrapperException.class)
    public void testMissingProperty()
        {
        Country serbia = new Country("SRB", "Serbia");
        ValueExtractor ex = new PropertyExtractor("xyz");

        ex.extract(serbia);
        }

    @Test(expected = RuntimeException.class)
    public void testNonReadableProperty()
        {
        Object obj = new ClassWithNonReadableProperty();
        ValueExtractor ex = new PropertyExtractor("nonReadable");

        ex.extract(obj);
        }


    // --- Inner class: ClassWithNonReadableProperty ------------------------

    private static class ClassWithNonReadableProperty
        {
        private Object nonReadable;

        public void setNonReadable(Object nonReadable)
            {
            this.nonReadable = nonReadable;
            }
        }
    }