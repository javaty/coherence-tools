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

package com.seovic.coherence.util.extractor;


import com.tangosol.util.ValueExtractor;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import com.seovic.lang.Extractor;

import java.io.Serializable;
import java.io.IOException;


/**
 * Adapter that allows any class that implements <tt>com.tangosol.util.ValueExtractor</tt>
 * to be used where {@link Extractor} instance is expected.
 *
 * @author Aleksandar Seovic  2009.09.20
 */
public class ValueExtractorAdapter
    implements Extractor, Serializable, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public ValueExtractorAdapter()
        {
        }

    /**
     * Construct a <tt>ValueExtractorAdapter</tt> instance.
     *
     * @param delegate  value extractor to delegate to
     */
    public ValueExtractorAdapter(ValueExtractor delegate)
        {
        this.m_delegate = delegate;
        }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object o)
        {
        return m_delegate.extract(o);
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
        m_delegate = (ValueExtractor) reader.readObject(0);
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
        writer.writeObject(0, m_delegate);
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

        ValueExtractorAdapter adapter = (ValueExtractorAdapter) o;
        return m_delegate.equals(adapter.m_delegate);
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return m_delegate.hashCode();
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "ValueExtractorAdapter{" +
               "delegate=" + m_delegate +
               '}';
        }

    
    // ---- data members ----------------------------------------------------

    private ValueExtractor m_delegate;
    }
