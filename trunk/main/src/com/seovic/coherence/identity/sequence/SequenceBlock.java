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

package com.seovic.coherence.identity.sequence;


import java.io.Serializable;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicLong;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;


/**
 * Represents a block of sequential numbers.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
public class SequenceBlock
        implements Serializable, PortableObject
    {
    // ---- data members ----------------------------------------------------

    /**
     * The next assignable number within this sequence block.
     */
    private AtomicLong m_next;

    /**
     * The last assignable number within this sequence block.
     */
    private long m_last;


    // ---- constructors ----------------------------------------------------

    /**
     * For internal use only.
     */
    public SequenceBlock()
        {
        // deserialization constructor
        }

    /**
     * Construct a new sequence block.
     *
     * @param first  first number in a sequence
     * @param last   last number in a sequence
     */
    public SequenceBlock(long first, long last)
        {
        m_next = new AtomicLong(first);
        m_last = last;
        }


    // ---- public methods --------------------------------------------------

    /**
     * Return <tt>true</tt> if there are avialable numbers in this
     * sequence block, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if there are avialable numbers in this
     *         sequence block, <tt>false</tt> otherwise
     */
    public boolean hasNext()
        {
        return m_next.longValue() <= m_last;
        }

    /**
     * Return the next available number in this sequence block.
     *
     * @return the next available number in this sequence block
     */
    public long next()
        {
        return m_next.getAndIncrement();
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize object from the POF stream.
     *
     * @param pofReader  POF reader to use
     *
     * @throws IOException  if an error occurs
     */
    public void readExternal(PofReader pofReader)
            throws IOException
        {
        m_next = new AtomicLong(pofReader.readLong(0));
        m_last = pofReader.readLong(1);
        }

    /**
     * Serialize object into the POF stream.
     *
     * @param pofWriter  POF writer to use
     *
     * @throws IOException  if an error occurs
     */
    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeLong(0, m_next.longValue());
        pofWriter.writeLong(1, m_last);
        }
    }
