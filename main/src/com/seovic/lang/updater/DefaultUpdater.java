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

package com.seovic.lang.updater;


import com.seovic.lang.Updater;

import java.util.Map;


/**
 * Composite imlementation of {@link Updater} that updates value using
 * either a {@link BeanUpdater} or a {@link MapUpdater}, depending on the
 * type of update target.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class DefaultUpdater
        implements Updater {
    // ---- data members ----------------------------------------------------

    private Updater mapUpdater;
    private Updater beanUpdater;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>DefaultUpdater</tt> instance.
     *
     * @param propertyName  the name of the property or key to update
     */
    public DefaultUpdater(String propertyName) {
        mapUpdater  = new MapUpdater(propertyName);
        beanUpdater = new BeanUpdater(propertyName);
    }


    // ---- Updater implementation ------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void update(Object target, Object value) {
        if (target == null) {
            throw new IllegalArgumentException("Updater target cannot be null");
        }
        else if (target instanceof Map) {
            mapUpdater.update(target, value);
        }
        else {
            beanUpdater.update(target, value);
        }
    }
}