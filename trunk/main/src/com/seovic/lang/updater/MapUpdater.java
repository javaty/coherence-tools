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
 * Simple imlementation of {@link Updater} that updates single map entry.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class MapUpdater
        implements Updater {
    // ---- data members ----------------------------------------------------

    private String key;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>MapUpdater</tt> instance.
     *
     * @param key  the key of the entry to update
     */
    public MapUpdater(String key) {
        this.key = key;
    }


    // ---- Updater implementation ------------------------------------------

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked"})
    public void update(Object target, Object value) {
        if (target == null) {
            throw new IllegalArgumentException("Updater target cannot be null");
        }
        if (!(target instanceof Map)) {
            throw new IllegalArgumentException("Updater target is not a Map");
        }

        ((Map) target).put(key, value);
    }
}