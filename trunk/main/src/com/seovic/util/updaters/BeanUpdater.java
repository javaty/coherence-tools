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

package com.seovic.util.updaters;


import com.seovic.util.Updater;

import java.lang.reflect.Method;


/**
 * Simple imlementation of {@link Updater} that updates single property
 * of a target value using introspection.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
@SuppressWarnings("unchecked")
public class BeanUpdater
        implements Updater {
    // ---- data members ----------------------------------------------------

    private String propertyName;

    private transient Method mutator;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>BeanExtractor</tt> instance.
     *
     * @param propertyName the name of the property to extract, as defined by
     *                     the JavaBean specification
     */
    public BeanUpdater(String propertyName) {
        this.propertyName = propertyName;
    }


    // ---- Updater implementation ------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void update(Object target, Object value) {
        if (target == null) {
            throw new IllegalArgumentException("Updater target cannot be null");
        }

        Class targetClass = target.getClass();
        try {
            Method method = mutator;
            if (method == null
                || method.getDeclaringClass() != targetClass) {
                mutator = method = findWriteMethod(propertyName,
                                                   target.getClass(),
                                                   value == null ? Object.class : value.getClass());
            }
            method.invoke(target, value);
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
     * Attempt to find a write method for the specified property name.
     * <p/>
     * This method attempts to find a write method by prepending 'set' prefix
     * to the specified property name.
     *
     * @param propertyName  property name
     * @param cls           class containing the property
     * @param propertyType  property type
     * 
     * @return write method for the property, or <tt>null</tt> if the method
     *         cannot be found
     */
    protected Method findWriteMethod(String propertyName, Class cls, Class propertyType) {
        String name = "set"
                      + Character.toUpperCase(propertyName.charAt(0))
                      + propertyName.substring(1);

        try {
            return cls.getMethod(name, propertyType);
        }
        catch (NoSuchMethodException ignore) {}

        return null;
    }
}