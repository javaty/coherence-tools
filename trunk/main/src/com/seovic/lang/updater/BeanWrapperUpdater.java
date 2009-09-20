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
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


/**
 * Simple imlementation of {@link Updater} that updates single property
 * of a target object using Spring BeanWrapper, thus allowing for the automatic
 * conversion of String values to a target property type. 
 *
 * @author Aleksandar Seovic  2009.06.18
 */
public class BeanWrapperUpdater
        implements Updater {
    // ---- data members ----------------------------------------------------

    private String propertyName;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct a <tt>BeanWrapperUpdater</tt> instance.
     * 
     * @param propertyName  the name of the proeprty to update
     */
    public BeanWrapperUpdater(String propertyName) {
        this.propertyName = propertyName;
    }


    // ---- Updater implementation ------------------------------------------

    public void update(Object target, Object value) {
        BeanWrapper bw = new BeanWrapperImpl(target);
        bw.setPropertyValue(propertyName, value);
    }
}
