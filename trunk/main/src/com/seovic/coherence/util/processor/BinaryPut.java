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

package com.seovic.coherence.util.processor;


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import com.tangosol.util.BinaryEntry;

import java.io.IOException;


/**
 * An entry processor that puts entry into the cache and optionally returns
 * the old value.
 * <p/>
 * This processor has identical behavior to {@link Put}, but it avoids
 * unnecessary serialization within the cluster by updating BinaryEntry directly.
 * <p/>
 * This implies that it can only be used with the partitioned caches.
 *
 * @author Aleksandar Seovic  2009.09.29
 */
public class BinaryPut
        extends AbstractBinaryProcessor
        implements PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public BinaryPut()
        {
        }

    /**
     * Construct BinaryPut instance.
     *
     * @param value  value to put into the cache
     */
    public BinaryPut(Object value)
        {
        this(value, false);
        }

    /**
     * Construct BinaryPut instance.
     *
     * @param value       value to put into the cache
     * @param fReturnOld  flag specifying whether to return the old value
     */
    public BinaryPut(Object value, boolean fReturnOld)
        {
        m_value      = value;
        m_fReturnOld = fReturnOld;
        }


    // ---- AbstractProcessor implementation --------------------------------

    /**
     * Process specified entry and return the result.
     *
     * @param entry  entry to process
     *
     * @return processing result
     */
    public Object process(BinaryEntry entry)
        {
        Object oldValue = m_fReturnOld ? entry.getValue() : null;
        entry.updateBinaryValue(getBinaryValue("value"));
        return oldValue;
        }


    // ---- accessors -------------------------------------------------------

    /**
     * Return deserialized value.
     *
     * @return deserialized value
     */
    protected Object getValue()
        {
        Object value = m_value;
        if (value == null)
            {
            m_value = value = fromBinary("value");
            }
        return value;
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
        setBinaryValue("value", reader.readBinary(0));
        m_fReturnOld = reader.readBoolean(1);
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
        writer.writeBinary (0, toBinary(m_value));
        writer.writeBoolean(1, m_fReturnOld);
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

        BinaryPut processor = (BinaryPut) o;

        return super.equals(o)
               && m_fReturnOld == processor.m_fReturnOld
               && (m_value == null
                    ? processor.m_value == null
                    : m_value.equals(processor.m_value));
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        int result = super.hashCode();
        result = 31 * result + (m_value != null ? m_value.hashCode() : 0);
        result = 31 * result + (m_fReturnOld ? 1 : 0);
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
        return "BinaryPut{" +
               "value=" + getValue() +
               "fReturnOld=" + m_fReturnOld +
               '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * Value to put into the cache.
     */
    private Object m_value;

    /**
     * Flag specifying whether to return the old value.
     */
    private boolean m_fReturnOld;
    }