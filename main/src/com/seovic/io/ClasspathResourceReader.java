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


import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.CharBuffer;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;


/**
 * @author Aleksandar Seovic  2010.02.02
 */
public class ClasspathResourceReader
        extends Reader
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct reader for a classpath resource.
     *
     * @param resourceName  resource name
     *
     * @throws IOException  if an error occurs
     */
    public ClasspathResourceReader(String resourceName)
            throws IOException
        {
        Resource resource = new DefaultResourceLoader().getResource(resourceName);
        m_reader = new InputStreamReader(resource.getInputStream());
        }


    // ---- Reader implementation -------------------------------------------

    public int read(CharBuffer buffer)
            throws IOException
        {
        return m_reader.read(buffer);
        }

    public int read()
            throws IOException
        {
        return m_reader.read();
        }

    public int read(char[] chars)
            throws IOException
        {
        return m_reader.read(chars);
        }

    public int read(char[] chars, int i, int i1)
            throws IOException
        {
        return m_reader.read(chars, i, i1);
        }

    public long skip(long l)
            throws IOException
        {
        return m_reader.skip(l);
        }

    public boolean ready()
            throws IOException
        {
        return m_reader.ready();
        }

    public boolean markSupported()
        {
        return m_reader.markSupported();
        }

    public void mark(int i)
            throws IOException
        {
        m_reader.mark(i);
        }

    public void reset()
            throws IOException
        {
        m_reader.reset();
        }

    public void close()
            throws IOException
        {
        m_reader.close();
        }


    // ---- data members ----------------------------------------------------

    private final Reader m_reader;
    }
