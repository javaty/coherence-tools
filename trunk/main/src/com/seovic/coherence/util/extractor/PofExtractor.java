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

package com.seovic.coherence.util.extractor;


import com.seovic.coherence.io.pof.reflect.SimplePofPath;


/**
 * An extension of standard PofExtractor that adds several convenience
 * constructors.
 *
 * @author Aleksandar Seovic  2009.10.01
 */
public class PofExtractor
        extends com.tangosol.util.extractor.PofExtractor
    {
    /**
     * Construct PofExtractor instance.
     *
     * @param indices  an array of property indices
     */
    public PofExtractor(int... indices)
        {
        super(new SimplePofPath(indices));
        }
    }
