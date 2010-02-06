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

package com.seovic.identity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;
import java.io.IOException;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;


/**
 * Represents a named sequence.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
@Entity
@Table(name = "SEQUENCES")
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

    /**
     * Sequence constructor.
     *
     * @param name  sequence name
     * @param last  last assigned number
     */
    public Sequence(String name, long last)
        {
        m_name = name;
        m_last = last;
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
    public SequenceGenerator.SequenceBlock allocateBlock(int blockSize)
        {
        final long last = m_last;
        SequenceGenerator.SequenceBlock block =
                new SequenceGenerator.SequenceBlock(last + 1, last + blockSize);
        m_last = last + blockSize;

        return block;
        }

    /**
     * Return the sequence name.
     *
     * @return the sequence name
     */
    public String getName()
        {
        return m_name;
        }

    /**
     * Return the last allocated sequence number.
     *
     * @return the last allocated sequence number
     */
    public long getLast()
        {
        return m_last;
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize object from the POF stream.
     *
     * @param reader POF reader to use
     *
     * @throws IOException if an error occurs
     */
    public void readExternal(PofReader reader)
            throws IOException
        {
        m_name = reader.readString(0);
        m_last = reader.readLong(1);
        }

    /**
     * Serialize object into the POF stream.
     *
     * @param writer POF writer to use
     *
     * @throws IOException if an error occurs
     */
    public void writeExternal(PofWriter writer)
            throws IOException
        {
        writer.writeString(0, m_name);
        writer.writeLong(1, m_last);
        }


    // ---- Object methods --------------------------------------------------

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

        Sequence sequence = (Sequence) o;

        return m_last == sequence.m_last
               && m_name.equals(sequence.m_name);
        }

    @Override
    public int hashCode()
        {
        int result = m_name.hashCode();
        result = 31 * result + (int) (m_last ^ (m_last >>> 32));
        return result;
        }

    @Override
    public String toString()
        {
        return "Sequence(" +
               "name='" + m_name + '\'' +
               ", last=" + m_last +
               ')';
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
