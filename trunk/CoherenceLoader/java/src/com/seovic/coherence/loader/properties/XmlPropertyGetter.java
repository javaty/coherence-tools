package com.seovic.coherence.loader.properties;

import com.seovic.coherence.loader.PropertyGetter;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author ic  2009.06.15
 */
public class XmlPropertyGetter implements PropertyGetter {
    private String propertyName;
    private String namespace;

    public XmlPropertyGetter(String propertyName) {
        this.propertyName = propertyName;
    }

    public XmlPropertyGetter(String propertyName, String nsUri) {
        this.propertyName = propertyName;
        this.namespace    = nsUri;
    }

    public Object getValue(Object sourceItem) {
        Document sourceDoc     = (Document) sourceItem;
        Element  sourceElement = sourceDoc.getDocumentElement();
        String nsUri = namespace == null
                       ? sourceElement.getNamespaceURI()
                       : namespace; 
        if (sourceElement.hasAttributeNS(nsUri, propertyName)) {
            return sourceElement.getAttributeNS(nsUri, propertyName);
        }
        else {
            NodeList candidates = sourceElement.getElementsByTagNameNS(nsUri, propertyName);
            if (candidates.getLength() > 0) {
                return candidates.item(0).getTextContent();
            }
        }
        return null;
    }
}
