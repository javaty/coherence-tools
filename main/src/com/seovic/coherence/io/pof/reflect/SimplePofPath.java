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

package com.seovic.coherence.io.pof.reflect;


/**
 * An extension of standard SimplePofPath that adds convenience constructors.
 *
 * @author Aleksandar Seovic  2009.09.29
 */
public class SimplePofPath extends com.tangosol.io.pof.reflect.SimplePofPath
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct a SimplePofPath using an array of indices as a path.
     *
     * @param indices  an array of indices
     */
    public SimplePofPath(int... indices)
        {
        super(indices);
        }
    }
