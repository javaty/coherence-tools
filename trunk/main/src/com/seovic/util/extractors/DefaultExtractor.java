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
 * Composite imlementation of {@link Extractor} that extracts value using
 * either a {@link BeanExtractor} or a {@link MapExtractor}, depending on the
 * type of extraction target.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class DefaultExtractor
        implements Extractor {
    // ---- data members ----------------------------------------------------

    private Extractor mapExtractor;
    private Extractor beanExtractor;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>DefaultExtractor</tt> instance.
     *
     * @param propertyName  the name of the property or key to extract
     */
    public DefaultExtractor(String propertyName) {
        mapExtractor  = new MapExtractor(propertyName);
        beanExtractor = new BeanExtractor(propertyName);
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }
        else if (target instanceof Map) {
            return mapExtractor.extract(target);
        }
        else {
            return beanExtractor.extract(target);    
        }
    }
}