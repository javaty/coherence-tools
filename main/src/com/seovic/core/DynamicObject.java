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

package com.seovic.core;


import java.math.BigDecimal;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import java.io.Serializable;


/**
 * Object that supports dynamic properties.
 * 
 * @author Aleksandar Seovic  2009.11.05
 */
public class DynamicObject
        implements Serializable
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Default constructor.
     */
    public DynamicObject()
        {
        m_properties = createPropertyMap();
        }


    // ---- public API ------------------------------------------------------

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public Object getValue(String name)
        {
        return m_properties.get(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setValue(String name, Object value)
        {
        m_properties.put(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public boolean getBoolean(String name)
        {
        return (Boolean) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setBoolean(String name, boolean value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public byte getByte(String name)
        {
        return (Byte) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setByte(String name, byte value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public char getChar(String name)
        {
        return (Character) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setChar(String name, char value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public short getShort(String name)
        {
        return (Short) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setShort(String name, short value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public int getInt(String name)
        {
        return (Integer) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setInt(String name, int value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public long getLong(String name)
        {
        return (Long) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setLong(String name, long value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public double getDouble(String name)
        {
        return (Double) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setDouble(String name, double value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public BigDecimal getBigDecimal(String name)
        {
        return (BigDecimal) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setBigDecimal(String name, BigDecimal value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public String getString(String name)
        {
        return (String) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setString(String name, String value)
        {
        setValue(name, value);
        }

    /**
     * Return property value for the specified name.
     *
     * @param name property name
     *
     * @return value of the specified property
     */
    public Date getDate(String name)
        {
        return (Date) getValue(name);
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setDate(String name, Date value)
        {
        setValue(name, value);
        }

    /**
     * Merge all properties from the specified object into this one.
     * <p/>
     * Any properties with the same name that already exist in this object
     * will be overwritten.
     *
     * @param obj  object to merge into this object
     */
    public void merge(DynamicObject obj)
        {
        m_properties.putAll(obj.m_properties);
        }


    // ---- internal API ----------------------------------------------------

    /**
     * Factory method that creates internal property map.
     *
     * @return internal property map instance
     */
    protected Map<String, Object> createPropertyMap()
        {
        return new HashMap<String, Object>();
        }

    /**
     * Return internal property map.
     *
     * @return internal property map
     */
    protected Map<String, Object> getProperties()
        {
        return m_properties;
        }

    /**
     * Set internal property map.
     *
     * @param properties  internal property map
     */
    protected void setProperties(Map<String, Object> properties)
        {
        m_properties = properties;
        }


    // ---- Object methods --------------------------------------------------

    /**
     * Test objects for equality.
     *
     * @param o object to compare this object with
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
        if (o == null || !(o instanceof DynamicObject))
            {
            return false;
            }

        DynamicObject dynObj = (DynamicObject) o;
        return m_properties.equals(dynObj.m_properties);
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return m_properties.hashCode();
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return getClass().getSimpleName() + "{properties=" + m_properties + '}';
        }


    // ---- data members ----------------------------------------------------

    private Map<String, Object> m_properties;
    }
