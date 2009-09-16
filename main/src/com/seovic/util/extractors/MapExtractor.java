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

package com.seovic.util.extractors;


import com.seovic.util.Extractor;

import java.util.Map;


/**
 * Simple imlementation of {@link Extractor} that extracts value from a map.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class MapExtractor implements Extractor {
    // ---- data members ----------------------------------------------------

    private String key;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>MapExtractor</tt> instance.
     *
     * @param key  the key to extract value for
     */
    public MapExtractor(String key) {
        this.key = key;
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }
        if (!(target instanceof Map)) {
            throw new IllegalArgumentException("Extraction target is not a Map");
        }

        return ((Map) target).get(key);
    }
}
