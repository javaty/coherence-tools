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


import com.seovic.coherence.identity.IdentityGenerator;
import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;


/**
 * An {@link IdentityGenerator} implementation that generates sequential,
 * Long-based identifiers.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
public class SequenceGenerator
        implements IdentityGenerator<Long>
    {
    // ---- data members ----------------------------------------------------

    /**
     * Default sequence block size.
     */
    private static final int DEFAULT_BLOCK_SIZE = 20;

    /**
     * Sequences cache.
     */
    private static final NamedCache
            s_sequenceCache = CacheFactory.getCache("sequences");

    /**
     * Sequence name.
     */
    private String name;

    /**
     * Sequence block size.
     */
    private int blockSize;

    /**
     * Currently allocated block of sequences.
     */
    private SequenceBlock allocatedSequences;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct sequence generator.
     *
     * @param name  a sequence name
     */
    public SequenceGenerator(String name)
        {
        this(name, DEFAULT_BLOCK_SIZE);
        }

    /**
     * Construct sequence generator.
     *
     * @param name       a sequence name
     * @param blockSize  the size of the sequence block to allocate at once
     */
    public SequenceGenerator(String name, int blockSize)
        {
        this.name = name;
        this.blockSize = blockSize;
        }


    // ---- IdentityGenerator implementation --------------------------------

    /**
     * Return the next number in the sequence.
     *
     * @return the next number in the sequence
     */
    public synchronized Long generateIdentity()
        {
        if (allocatedSequences == null || !allocatedSequences.hasNext())
            {
            allocatedSequences = allocateSequenceBlock();
            }
        return allocatedSequences.next();
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Allocate a new sequence block.
     *
     * @return block of sequential numbers
     */
    protected SequenceBlock allocateSequenceBlock()
        {
        return (SequenceBlock)
                s_sequenceCache.invoke(name,
                                     new SequenceBlockAllocator(blockSize));
        }
    }
