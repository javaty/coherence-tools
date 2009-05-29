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


import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.InvocableMap;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;


/**
 * An entry processor that allocates a block of sequential number from a named
 * sequence.
 * <p/>
 * If the sequence entry for the given name does not already exist in the cache,
 * it will be created automatically.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
public class SequenceBlockAllocator
        extends    AbstractProcessor
        implements PortableObject
    {
    // ---- data members ----------------------------------------------------

    /**
     * The size of the sequence block to allocate.
     */
    private int m_blockSize;


    // ---- constructors ----------------------------------------------------

    /**
     * For internal use only.
     */
    public SequenceBlockAllocator()
        {
        // deserialization constructor
        }

    /**
     * Construct new sequence block allocator.
     *
     * @param blockSize  the size of the sequence block to allocate
     */
    public SequenceBlockAllocator(int blockSize)
        {
        this.m_blockSize = blockSize;
        }


    // ---- EntryProcessor implementation -----------------------------------

    /**
     * Allocates a block of sequences from a target entry.
     * <p/>
     * If the target entry for the given name does not already exist in a cache,
     * it will be created automatically.
     *
     * @param entry  target entry to allocate sequence block from
     *
     * @return allocated sequence block
     */
    public Object process(InvocableMap.Entry entry)
        {
        Sequence sequence = entry.isPresent()
                            ? (Sequence) entry.getValue()
                            : new Sequence();

        SequenceBlock block = sequence.allocateBlock(m_blockSize);
        entry.setValue(sequence);

        return block;
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
        m_blockSize = pofReader.readInt(0);
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
        pofWriter.writeInt(0, m_blockSize);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * Test specified object for equality.
     *
     * @param o  object to test
     *
     * @return <tt>true</tt> if the specified object is equal to this one,
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

        SequenceBlockAllocator that = (SequenceBlockAllocator) o;
        return m_blockSize == that.m_blockSize;
        }

    /**
     * Return a hash code for this object.
     *
     * @return a hash code for this object
     */
    @Override
    public int hashCode()
        {
        return m_blockSize;
        }

    /**
     * Return string representation of this object.
     * 
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "SequenceBlockAllocator{" +
               "blockSize=" + m_blockSize +
               '}';
        }
    }
