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
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import java.util.Map;
import java.io.Serializable;
import java.io.IOException;


/**
 * Composite imlementation of {@link Extractor} that extracts value using either
 * a {@link PropertyExtractor} or a {@link MapExtractor}, depending on the type
 * of extraction target.
 *
 * @author Aleksandar Seovic  2009.06.17
 */
public class DefaultExtractor
        implements Extractor, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public DefaultExtractor()
        {
        }

    /**
     * Construct a <tt>DefaultExtractor</tt> instance.
     *
     * @param propertyName  the name of the property or key to extract
     */
    public DefaultExtractor(String propertyName)
        {
        m_mapExtractor      = new MapExtractor(propertyName);
        m_propertyExtractor = new PropertyExtractor(propertyName);
        }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target)
        {
        if (target == null)
            {
            return null;
            }
        else if (target instanceof Map)
            {
            return m_mapExtractor.extract(target);
            }
        else
            {
            return m_propertyExtractor.extract(target);
            }
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
        m_mapExtractor      = (Extractor) reader.readObject(0);
        m_propertyExtractor = (Extractor) reader.readObject(1);
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
        writer.writeObject(0, m_mapExtractor);
        writer.writeObject(1, m_propertyExtractor);
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

        DefaultExtractor that = (DefaultExtractor) o;

        return m_mapExtractor.equals(that.m_mapExtractor)
               && m_propertyExtractor.equals(that.m_propertyExtractor);
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        int result = m_mapExtractor.hashCode();
        result = 31 * result + m_propertyExtractor.hashCode();
        return result;
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "DefaultExtractor{" +
               "mapExtractor=" + m_mapExtractor +
               ", propertyExtractor=" + m_propertyExtractor +
               '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * Map extractor.
     */
    private Extractor m_mapExtractor;

    /**
     * Property extractor.
     */
    private Extractor m_propertyExtractor;
    }