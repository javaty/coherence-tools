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

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.AccessType;
import javax.persistence.Access;


/**
 * Represents a named sequence.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
@Entity
@Table(name = "COH_TOOLS_SEQUENCES")
@Access(AccessType.FIELD)
public class Sequence
        implements Serializable, PortableObject
    {
    // ---- constructors --------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public Sequence()
        {
        }

    /**
     * Sequence constructor.
     *
     * @param name sequence name
     */
    public Sequence(String name)
        {
        m_name = name;
        }

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
        final long last = m_last;
        SequenceBlock block = new SequenceBlock(last + 1, last + blockSize);
        m_last = last + blockSize;

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

    /**
     * Return the sequence name.
     *
     * @return the sequence name
     */
    public String name()
        {
        return m_name;
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
        m_name = pofReader.readString(0);
        m_last = pofReader.readLong(1);
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
        pofWriter.writeString(0, m_name);
        pofWriter.writeLong(1, m_last);
        }


    // ---- data members ----------------------------------------------------

    /**
     * Sequence name.
     */
    @Id
    @Column(name = "NAME", nullable = false)
    private String m_name;

    /**
     * The last allocated number from this sequence.
     */
    @Column(name = "LAST_SEQ", nullable = false)
    private long m_last;
    }
