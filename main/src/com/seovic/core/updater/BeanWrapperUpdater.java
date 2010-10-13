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

package com.seovic.core.updater;


import com.seovic.core.Updater;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.io.Serializable;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Simple imlementation of {@link Updater} that updates single property of a
 * target object using Spring BeanWrapper, thus allowing for the automatic
 * conversion of String values to a target property type.
 *
 * @author Aleksandar Seovic  2009.06.18
 */
public class BeanWrapperUpdater
        implements Updater, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public BeanWrapperUpdater()
        {
        }

    /**
     * Construct a <tt>BeanWrapperUpdater</tt> instance.
     *
     * @param propertyName the name of the property to update
     */
    public BeanWrapperUpdater(String propertyName)
        {
        m_propertyName = propertyName;
        }


    // ---- Updater implementation ------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void update(Object target, Object value)
        {
        BeanWrapper bw = new BeanWrapperImpl(target);
        if (value instanceof Date)
            {
            bw.registerCustomEditor(Date.class,
                                    new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
            }
        bw.setPropertyValue(m_propertyName, value);
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize this object from a POF stream.
     *
     * @param reader  POF reader to use
     *
     * @throws IOException  if an error occurs during deserialization
     */
    public void readExternal(PofReader reader)
            throws IOException
        {
        m_propertyName = reader.readString(0);
        }

    /**
     * Serialize this object into a POF stream.
     *
     * @param writer  POF writer to use
     *
     * @throws IOException  if an error occurs during serialization
     */
    public void writeExternal(PofWriter writer)
            throws IOException
        {
        writer.writeString(0, m_propertyName);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * Test objects for equality.
     *
     * @param o  object to compare this object with
     *
     * @return <tt>true</tt> if the specified object is equal to this object
     *         <tt>false</tt> otherwise
     */
    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        BeanWrapperUpdater that = (BeanWrapperUpdater) o;
        return m_propertyName.equals(that.m_propertyName);
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return m_propertyName.hashCode();
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "BeanWrapperUpdater{" +
               "propertyName='" + m_propertyName + '\'' +
               '}';
        }

    // ---- data members ----------------------------------------------------

    /**
     * Property name.
     */
    private String m_propertyName;
    }
