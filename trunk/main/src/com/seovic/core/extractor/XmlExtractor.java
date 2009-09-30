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
 * @author Aleksandar Seovic  2009.06.18
 */
public class XmlExtractor implements Extractor, PortableObject
    {
    // ---- data members ----------------------------------------------------

    private String nodeName;
    private String namespace;


    // ---- constructors ----------------------------------------------------

    /**
     * Construct <tt>XmlExtractor</tt> instance.
     *
     * @param nodeName  the name of attribute or child element to extract
     */
    public XmlExtractor(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Construct <tt>XmlExtractor</tt> instance.
     *
     * @param nodeName  the name of attribute or child element to extract
     * @param nsUri     namespace URI of the attribute or child element to extract
     */
    public XmlExtractor(String nodeName, String nsUri) {
        this.nodeName  = nodeName;
        this.namespace = nsUri;
    }


    // ---- Extractor implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object extract(Object target) {
        if (target == null) {
            return null;
        }

        Document sourceDoc     = (Document) target;
        Element  sourceElement = sourceDoc.getDocumentElement();
        String   nsUri         = namespace == null
                                  ? sourceElement.getNamespaceURI()
                                  : namespace;

        if (sourceElement.hasAttributeNS(nsUri, nodeName)) {
            return sourceElement.getAttributeNS(nsUri, nodeName);
        }
        else {
            NodeList candidates = sourceElement.getElementsByTagNameNS(nsUri, nodeName);
            if (candidates.getLength() > 0) {
                return candidates.item(0).getTextContent();
            }
        }
        return null;
    }

    
    // ---- PortableObject implementation -----------------------------------
    
	public void readExternal(PofReader reader) throws IOException {
		nodeName  = reader.readString(0);
		namespace = reader.readString(1);
		
	}

	public void writeExternal(PofWriter writer) throws IOException {
		writer.writeString(0, nodeName);
		writer.writeString(1, namespace);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeName.hashCode();
		result = prime * result
		+ ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof XmlExtractor))
			return false;
		final XmlExtractor other = (XmlExtractor) obj;
		if (!namespace.equals(other.namespace))
			return false;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		} else if (!nodeName.equals(other.nodeName))
			return false;
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
               "nodeName='" + nodeName + '\'' +
               "namespace='" + namespace + '\'' +
               '}';
        }
}
