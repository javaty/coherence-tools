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

import java.lang.reflect.Method;


/**
 * Simple imlementation of {@link Extractor} that extracts value from a
 * target object using introspection.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
@SuppressWarnings("unchecked")
public class BeanExtractor
        implements Extractor {
    // ---- data members ----------------------------------------------------

    private String propertyName;

    private transient Method accessor;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>BeanExtractor</tt> instance.
     *
     * @param propertyName the name of the property to extract, as defined by
     *                     the JavaBean specification
     */
    public BeanExtractor(String propertyName) {
        this.propertyName = propertyName;
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }

        Class targetClass = target.getClass();
        try {
            Method method = accessor;
            if (method == null
                || method.getDeclaringClass() != targetClass) {
                accessor = method = findReadMethod(propertyName,
                                                   target.getClass());
            }
            return method.invoke(target);
        }
        catch (NullPointerException e) {
            throw new RuntimeException("Property " + propertyName +
                                       " does not exist in the class " + targetClass);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // ---- helper methods --------------------------------------------------

    /**
     * Attempt to find a read method for the specified property name.
     * <p/>
     * This method attempts to find a read method by prepending prefixes 'get', 'is'
     * and 'has' to the specified property name, in that order.
     *
     * @param propertyName  property name
     * @param cls           class containing the property
     *
     * @return read method for the property, or <tt>null</tt> if the method cannot
     *         be found
     */
    protected Method findReadMethod(String propertyName, Class cls) {
        String name = Character.toUpperCase(propertyName.charAt(0))
                      + propertyName.substring(1);

        final Class[]  EMPTY_ARGS = new Class[0];
        final String[] prefixes   = new String[] {"get", "is", "has"};

        for (String prefix : prefixes) {
            try {
                return cls.getMethod(prefix + name, EMPTY_ARGS);
            }
            catch (NoSuchMethodException ignore) {
            }
        }

        return null;
    }
}
