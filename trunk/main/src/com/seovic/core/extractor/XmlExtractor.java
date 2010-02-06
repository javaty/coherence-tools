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

package com.seovic.core.extractor;


import java.io.IOException;

import com.seovic.core.Extractor;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Implementation of {@link Extractor} that extracts the value of an attribute
 * or a child element from the target XML node.
 * 
 * @author Aleksandar Seovic  2009.06.18
 */
public class XmlExtractor
        implements Extractor, PortableObject
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor (for internal use only).
     */
    public XmlExtractor()
        {
        }

    /**
     * Construct <tt>XmlExtractor</tt> instance.
     *
     * @param nodeName  the name of the attribute or child element to extract
     */
    public XmlExtractor(String nodeName)
        {
        this.m_nodeName = nodeName;
        }

    /**
     * Construct <tt>XmlExtractor</tt> instance.
     *
     * @param nodeName the name of attribute or child element to extract
     * @param nsUri    namespace URI of the attribute or child element to
     *                 extract
     */
    public XmlExtractor(String nodeName, String nsUri)
        {
        this.m_nodeName = nodeName;
        this.m_namespace = nsUri;
        }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target)
        {
        if (target == null)
            {
            return null;
            }

        Document sourceDoc    = (Document) target;
        Element sourceElement = sourceDoc.getDocumentElement();
        String nsUri          = m_namespace == null
                                ? sourceElement.getNamespaceURI()
                                : m_namespace;

        if (sourceElement.hasAttributeNS(nsUri, m_nodeName))
            {
            return sourceElement.getAttributeNS(nsUri, m_nodeName);
            }
        else
            {
            NodeList candidates = sourceElement.getElementsByTagNameNS(nsUri,
                                                                       m_nodeName);
            if (candidates.getLength() > 0)
                {
                return candidates.item(0).getTextContent();
                }
            }
        return null;
        }


    // ---- PortableObject implementation -----------------------------------

    public void readExternal(PofReader reader)
            throws IOException
        {
        m_nodeName = reader.readString(0);
        m_namespace = reader.readString(1);

        }

    public void writeExternal(PofWriter writer)
            throws IOException
        {
        writer.writeString(0, m_nodeName);
        writer.writeString(1, m_namespace);
        }

    @Override
    public int hashCode()
        {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_nodeName.hashCode();
        result = prime * result
                 + ((m_namespace == null) ? 0 : m_namespace.hashCode());
        return result;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (this == obj)
            {
            return true;
            }
        if (obj == null)
            {
            return false;
            }
        if (!(obj instanceof XmlExtractor))
            {
            return false;
            }
        final XmlExtractor other = (XmlExtractor) obj;
        if (!m_namespace.equals(other.m_namespace))
            {
            return false;
            }
        if (m_nodeName == null)
            {
            if (other.m_nodeName != null)
                {
                return false;
                }
            }
        else if (!m_nodeName.equals(other.m_nodeName))
            {
            return false;
            }
        return true;
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "XmlExtractor{" +
               "nodeName='" + m_nodeName + '\'' +
               "namespace='" + m_namespace + '\'' +
               '}';
        }


    // ---- data members ----------------------------------------------------

    private String m_nodeName;
    private String m_namespace;
    }

