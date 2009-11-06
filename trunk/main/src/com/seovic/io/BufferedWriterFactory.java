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

package com.seovic.io;


import java.io.Writer;
import java.io.Serializable;
import java.io.IOException;
import java.io.BufferedWriter;


/**
 * A {@link WriterFactory} implementation that creates a BufferedWriter.
 *
 * @author Aleksandar Seovic  2009.11.06
 */
public class BufferedWriterFactory
        implements WriterFactory, Serializable
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct BufferedWriterFactory instance.
     *
     * @param writerFactory  the writer factory whose product should be wrapped
     *                       with a BufferedWriter
     */
    public BufferedWriterFactory(WriterFactory writerFactory)
        {
        this(writerFactory, 0);
        }

    /**
     * Construct BufferedWriterFactory instance.
     *
     * @param writerFactory  the writer factory whose product should be wrapped
     *                       with a BufferedWriter
     * @param bufSize        buffer size in bytes
     */
    public BufferedWriterFactory(WriterFactory writerFactory, int bufSize)
        {
        m_writerFactory = writerFactory;
        m_bufSize       = bufSize;
        }


    // ---- WriterFactory implementation ------------------------------------

    /**
     * {@inheritDoc}
     */
    public Writer createWriter()
            throws IOException
        {
        return m_bufSize > 0
               ? new BufferedWriter(m_writerFactory.createWriter(), m_bufSize)
               : new BufferedWriter(m_writerFactory.createWriter());
        }


    // ---- data members ----------------------------------------------------

    /**
     * The writer factory whose product should be wrapped with a BufferedWriter.
     */
    private WriterFactory m_writerFactory;

    /**
     * Buffer size in bytes.
     */
    private int m_bufSize;
    }