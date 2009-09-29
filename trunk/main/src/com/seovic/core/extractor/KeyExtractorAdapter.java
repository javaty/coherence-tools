/*
 * Copyright 2009 Aleksandar Seovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seovic.core.extractor;


import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.KeyExtractor;

import com.tangosol.io.pof.PortableObject;

import com.seovic.core.Extractor;

import java.io.Serializable;


/**
 * Adapter that allows any class that implements <tt>com.tangosol.util.ValueExtractor</tt>
 * to be used where {@link Extractor} instance is expected.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class KeyExtractorAdapter
    extends KeyExtractor
    implements Extractor, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public KeyExtractorAdapter()
        {
        }

    /**
     * Construct a <tt>KeyExtractorAdapter</tt> instance.
     *
     * @param delegate  value extractor to delegate to
     */
    public KeyExtractorAdapter(ValueExtractor delegate)
        {
        super(delegate);
        }
    }