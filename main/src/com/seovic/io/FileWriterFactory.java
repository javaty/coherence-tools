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
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;


/**
 * A {@link WriterFactory} implementation that creates a FileWriter.
 *
 * @author Aleksandar Seovic  2009.11.06
 */
public class FileWriterFactory
        implements WriterFactory, Serializable
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct FileWriterFactory instance.
     *
     * @param fileName  the name of the file to create writer for
     */
    public FileWriterFactory(String fileName)
        {
        this(new File(fileName));
        }

    /**
     * Construct FileWriterFactory instance.
     *
     * @param fileName  the name of the file to create writer for
     * @param fAppend   flag specifying whether to append to an existing file
     *                  or overwrite it
     */
    public FileWriterFactory(String fileName, boolean fAppend)
        {
        this(new File(fileName), fAppend);
        }

    /**
     * Construct FileWriterFactory instance.
     *
     * @param file  the file to create writer for
     */
    public FileWriterFactory(File file)
        {
        this(file, false);
        }

    /**
     * Construct FileWriterFactory instance.
     *
     * @param file     the file to create writer for
     * @param fAppend  flag specifying whether to append to an existing file
     *                 or overwrite it
     */
    public FileWriterFactory(File file, boolean fAppend)
        {
        m_file    = file;
        m_fAppend = fAppend;
        }


    // ---- WriterFactory implementation ------------------------------------

    /**
     * {@inheritDoc}
     */
    public Writer createWriter()
            throws IOException
        {
        return new FileWriter(m_file, m_fAppend);
        }


    // ---- data members ----------------------------------------------------

    /**
     * The file to create writer for.
     */
    private File m_file;

    /**
     * Flag specifying whether to append to an existing file or overwrite it.
     */
    private boolean m_fAppend;
    }
