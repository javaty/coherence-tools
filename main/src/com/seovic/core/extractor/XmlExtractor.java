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


import com.seovic.core.Extractor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @author Aleksandar Seovic  2009.06.18
 */
public class XmlExtractor implements Extractor
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
}
