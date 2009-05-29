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


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;
import java.io.Serializable;


/**
 * Represents a named sequence.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
public class Sequence
        implements Serializable, PortableObject
    {
    // ---- data members ----------------------------------------------------

    /**
     * The last allocated number from this sequence.
     */
    private long m_last;


    // ---- public methods --------------------------------------------------

    /**
     * Allocate a block of sequence numbers, starting from the last allocated
     * sequence value.
     *
     * @param blockSize the number of sequences to allocate
     *
     * @return allocated block of sequential numbers
     */
    public SequenceBlock allocateBlock(int blockSize)
        {
        SequenceBlock block = new SequenceBlock(m_last + 1, m_last + blockSize);
        m_last += blockSize;

        return block;
        }

    /**
     * Return the last allocated sequence number.
     *
     * @return the last allocated sequence number
     */
    public long peek()
        {
        return m_last;
        }

    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize object from the POF stream.
     *
     * @param pofReader POF reader to use
     *
     * @throws IOException if an error occurs
     */
    public void readExternal(PofReader pofReader)
            throws IOException
        {
        m_last = pofReader.readLong(0);
        }

    /**
     * Serialize object into the POF stream.
     *
     * @param pofWriter POF writer to use
     *
     * @throws IOException if an error occurs
     */
    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeLong(0, m_last);
        }
    }
