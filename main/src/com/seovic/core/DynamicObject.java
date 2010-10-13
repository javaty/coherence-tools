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


import com.seovic.util.Convert;
import com.seovic.util.ReflectionUtils;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.Serializable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;
import java.text.SimpleDateFormat;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;


/**
 * Object that supports dynamic properties.
 * 
 * @author Aleksandar Seovic  2009.11.05
 */
@XmlRootElement(name = "object")
@XmlAccessorType(XmlAccessType.NONE)
public class DynamicObject
        implements Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Default constructor.
     */
    public DynamicObject()
        {
        m_properties = createPropertyMap();
        }

    /**
     * Construct <tt>DynamicObject</tt> based on existing JavaBean.
     * <p/>
     * Constructed object will contain all readable properties of the specified
     * JavaBean.
     *
     * @param bean  a JavaBean to initialize this dynamic object with
     */
    public DynamicObject(Object bean)
        {
        m_properties = createPropertyMap();
        merge(bean);
        }

    /**
     * Construct <tt>DynamicObject</tt> based on existing JavaBean.
     * <p/>
     * Constructed object will contain only specific properties of the specified
     * JavaBean.
     *
     * @param bean        a JavaBean to initialize this dynamic object with
     * @param properties  properties to extract from the specified JavaBean
     */
    public DynamicObject(Object bean, PropertyList properties)
        {
        m_properties = createPropertyMap();
        merge(bean, properties);
        }

    /**
     * Construct <tt>DynamicObject</tt> based on existing Map.
     * <p/>
     * Constructed object will contain all entries from the specified map.
     *
     * @param map  a map to initialize this dynamic object with
     */
    public DynamicObject(Map<String, Object> map)
        {
        m_properties = createPropertyMap();
        merge(map);
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
        return Convert.toBoolean(getValue(name));
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
        return Convert.toByte(getValue(name));
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
        return Convert.toChar(getValue(name));
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
        return Convert.toShort(getValue(name));
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
        return Convert.toInt(getValue(name));
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
        return Convert.toLong(getValue(name));
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
    public float getFloat(String name)
        {
        return Convert.toFloat(getValue(name));
        }

    /**
     * Set value of the specified property.
     *
     * @param name   property name
     * @param value  property value
     */
    public void setFloat(String name, float value)
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
        return Convert.toDouble(getValue(name));
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
        return Convert.toBigDecimal(getValue(name));
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
        return getValue(name).toString();
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
        return Convert.toDate(getValue(name));
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
     * Merge all properties from the specified dynamic object into this one.
     * <p/>
     * Any properties with the same name that already exist in this object
     * will be overwritten.
     *
     * @param obj  object to merge into this object
     */
    public void merge(DynamicObject obj)
        {
        if (obj == null)
            {
            throw new IllegalArgumentException("Object to merge cannot be null");
            }

        m_properties.putAll(obj.m_properties);
        }

    /**
     * Merge all properties from the specified object into this one.
     * <p/>
     * Any properties with the same name that already exist in this object
     * will be overwritten.
     *
     * @param obj  object to merge into this object
     */
    public void merge(Object obj)
        {
        if (obj == null)
            {
            throw new IllegalArgumentException("Object to merge cannot be null");
            }

        m_properties.putAll(ReflectionUtils.getPropertyMap(obj));
        }

    /**
     * Merge specified properties from the specified object into this one.
     * <p/>
     * Any properties with the same name that already exist in this object
     * will be overwritten.
     *
     * @param obj         object to merge into this object
     * @param properties  properties to merge
     */
    public void merge(Object obj, PropertyList properties)
        {
        if (obj == null)
            {
            throw new IllegalArgumentException("Object to merge cannot be null");
            }

        Map<String, Object> propertyMap = ReflectionUtils.getPropertyMap(obj);

        for (PropertySpec property : properties)
            {
            String name = property.getName();
            Object value = propertyMap.get(name);

            if (value != null && property.getPropertyList() != null)
                {
                value = new DynamicObject(value, property.getPropertyList());
                }

            m_properties.put(name, value);
            }
        }

    /**
     * Merge all entries from the specified map into this object.
     * <p/>
     * Any properties with the same name that already exist in this object
     * will be overwritten.
     *
     * @param map  ma[ to merge into this object
     */
    public void merge(Map<String, Object> map)
        {
        if (map == null)
            {
            throw new IllegalArgumentException("Map to merge cannot be null");
            }

        m_properties.putAll(map);
        }

    /**
     * Update specified target from this object.
     *
     * @param target  target object to update
     */
    public void update(Object target)
        {
        if (target == null)
            {
            throw new IllegalArgumentException("Target to update cannot be null");
            }

        BeanWrapper bw = new BeanWrapperImpl(target);
        bw.registerCustomEditor(Date.class,
                                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        for (Map.Entry<String, Object> property : m_properties.entrySet())
            {
            String propertyName = property.getKey();
            Object value = property.getValue();

            if (value instanceof DynamicObject)
                {
                ((DynamicObject) value).update(bw.getPropertyValue(propertyName));
                }
            else
                {
                bw.setPropertyValue(propertyName, value);
                }
            }
        }


    // ---- internal API ----------------------------------------------------

    /**
     * Factory method that creates internal property map.
     *
     * @return internal property map instance
     */
    protected Map<String, Object> createPropertyMap()
        {
        return new LinkedHashMap<String, Object>();
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


    // ---- PortableObject implementation -----------------------------------

    @Override
    @SuppressWarnings({"unchecked"})
    public void readExternal(PofReader reader) throws IOException {
        reader.readMap(0, m_properties);
    }

    @Override
    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeMap(0, m_properties);
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

    @XmlElement(name = "properties")
    private Map<String, Object> m_properties;
    }
