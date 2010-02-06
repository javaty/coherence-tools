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

package com.seovic.identity;


import com.seovic.config.Configuration;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.Serializable;
import java.io.IOException;

import java.lang.reflect.Constructor;

import java.util.concurrent.atomic.AtomicLong;


/**
 * An abstract base class for {@link IdentityGenerator} implementations
 * that generate sequential Long identifiers.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
public abstract class SequenceGenerator
        implements IdentityGenerator<Long>, Serializable
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct sequence generator.
     *
     * @param name       a sequence name
     * @param blockSize  the size of the sequence block to allocate at once
     */
    protected SequenceGenerator(String name, int blockSize)
        {
        this.m_name      = name;
        this.m_blockSize = blockSize;
        }

    public static SequenceGenerator create(String name)
        {
        return create(name, DEFAULT_BLOCK_SIZE);
        }

    public static SequenceGenerator create(String name, int blockSize)
        {
        try
            {
            Class type = Configuration.getSequenceGeneratorType();
            Constructor ctor = type.getConstructor(String.class, int.class);
            return (SequenceGenerator) ctor.newInstance(name, blockSize);
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }

    
    // ---- properties ------------------------------------------------------

    public String getSequenceName()
        {
        return m_name;
        }

    public int getBlockSize()
        {
        return m_blockSize;
        }


    // ---- IdentityGenerator implementation --------------------------------

    /**
     * Return the next number in the sequence.
     *
     * @return the next number in the sequence
     */
    public synchronized Long generateIdentity()
        {
        SequenceBlock allocatedSequences = m_allocatedSequences;
        if (allocatedSequences == null || !allocatedSequences.hasNext())
            {
            m_allocatedSequences = allocatedSequences = allocateSequenceBlock();
            }
        return allocatedSequences.next();
        }


    // ---- abstract methods --------------------------------------------------

    /**
     * Allocate a new sequence block.
     *
     * @return block of sequential numbers
     */
    protected abstract SequenceBlock allocateSequenceBlock();


    // ---- data members ----------------------------------------------------

    /**
     * Default sequence block size.
     */
    public static final int DEFAULT_BLOCK_SIZE = 20;

    /**
     * Sequence name.
     */
    private final String m_name;

    /**
     * Sequence block size.
     */
    private final int m_blockSize;

    /**
     * Currently allocated block of sequences.
     */
    private SequenceBlock m_allocatedSequences;

    /**
 * Represents a block of sequential numbers.
     *
     * @author Aleksandar Seovic  2009.05.27
     */
    public static class SequenceBlock
            implements Serializable, PortableObject
        {
        // ---- constructors ----------------------------------------------------

        /**
         * Deserialization constructor (for internal use only).
         */
        public SequenceBlock()
            {
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
         * @param reader  POF reader to use
         *
         * @throws IOException  if an error occurs
         */
        public void readExternal(PofReader reader)
                throws IOException
            {
            m_next = new AtomicLong(reader.readLong(0));
            m_last = reader.readLong(1);
            }

        /**
         * Serialize object into the POF stream.
         *
         * @param writer  POF writer to use
         *
         * @throws IOException  if an error occurs
         */
        public void writeExternal(PofWriter writer)
                throws IOException
            {
            writer.writeLong(0, m_next.longValue());
            writer.writeLong(1, m_last);
            }


        // ---- data members ----------------------------------------------------

        /**
         * The next assignable number within this sequence block.
         */
        private AtomicLong m_next;

        /**
         * The last assignable number within this sequence block.
         */
        private volatile long m_last;
        }
    }
