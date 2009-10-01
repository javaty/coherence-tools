package com.seovic.coherence.util.processor;


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.InvocableMap;

import java.io.IOException;


/**
 * An entry processor that removes entry from the cache and optionally returns
 * the old value.
 * 
 * @author Aleksandar Seovic  2009.06.24
 */
public class Remove
        extends AbstractProcessor
        implements PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Default constructor.
     */
    public Remove()
        {
        }

    /**
     * Construct Remove processor instance.
     *
     * @param fReturnOld  flag specifying whether to return the old value
     */
    public Remove(boolean fReturnOld)
        {
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
    public Object process(InvocableMap.Entry entry)
        {
        Object oldValue = m_fReturnOld ? entry.getValue() : null;
        entry.remove(false);
        return oldValue;
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
        m_fReturnOld = reader.readBoolean(0);
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
        writer.writeBoolean(0, m_fReturnOld);
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

        Remove processor = (Remove) o;
        return m_fReturnOld == processor.m_fReturnOld;
        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return (m_fReturnOld ? 1 : 0);
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "Remove{" +
               "fReturnOld=" + m_fReturnOld +
               '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * Flag specifying whether to return the old value.
     */
    private boolean m_fReturnOld;
    }

