/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.coherence.util.extractor;


import com.tangosol.util.ValueExtractor;
import com.tangosol.util.WrapperException;
import com.tangosol.util.extractor.AbstractExtractor;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.beans.PropertyDescriptor;

import java.io.IOException;

import java.lang.reflect.Method;


/**
 * A simple implementation of a ValueExtractor that uses introspection to
 * retrieve property value.
 *
 * @author Aleksandar Seovic  2008.12.19
 */
public class PropertyExtractor
        extends    AbstractExtractor
        implements ValueExtractor, PortableObject
    {
    // ---- data members ----------------------------------------------------

    /**
     * The name of the property to extract.
     */
    private String m_propertyName;

    /**
     * Cached read method for the property represented by this extractor.
     */
    private transient Method m_readMethod;


    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor. For internal use only.
     */
    public PropertyExtractor()
        {
        }

    /**
     * Construct a <tt>PropertyExtractor</tt> instance.
     *
     * @param propertyName  the name of the property to extract
     */
    public PropertyExtractor(String propertyName)
        {
        m_propertyName = propertyName;
        }

    /**
     * Construct a <tt>PropertyExtractor</tt> instance.
     *
     * @param propertyName  the name of the property to extract
     * @param target        one of the {@link #VALUE} or {@link #KEY} values
     */
    public PropertyExtractor(String propertyName, int target)
        {
        m_propertyName = propertyName;
        m_nTarget      = target;
        }

    // ---- ValueExtractor implementation -----------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object o)
        {
        if (o == null)
            {
            return null;
            }

        Class targetClass = o.getClass();
        try
            {
            Method method = m_readMethod;
            if (method == null
                || method.getDeclaringClass() != targetClass)
                {
                PropertyDescriptor pd =
                        new PropertyDescriptor(m_propertyName, o.getClass());
                m_readMethod = method = pd.getReadMethod();
                }
            return method.invoke(o);
            }
        catch (Exception e)
            {
            throw new RuntimeException("Property " + m_propertyName +
                                       " of the class " + targetClass +
                                       " either does not exist or it cannot be read");
            }
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * {@inheritDoc}
     */
    public void readExternal(PofReader pofReader)
            throws IOException
        {
        m_propertyName = pofReader.readString(0);
        }

    /**
     * {@inheritDoc}
     */
    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeString(0, m_propertyName);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * {@inheritDoc}
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

        PropertyExtractor that = (PropertyExtractor) o;
        return m_propertyName.equals(that.m_propertyName);
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
        {
        return m_propertyName.hashCode();
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
        {
        return "PropertyExtractor(propertyName = " + m_propertyName + ")";
        }
    }
