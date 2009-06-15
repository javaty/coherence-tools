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


    public XmlPropertyGetter(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue(Object sourceItem) {
        Element sourceElement = ((Document) sourceItem).getDocumentElement();
        if (sourceElement.hasAttribute(propertyName)) {
            return sourceElement.getAttribute(propertyName);
        }
        else {
            NodeList candidates = sourceElement.getElementsByTagName(propertyName);
            if (candidates.getLength() > 0) {
                return candidates.item(0).getTextContent();
            }
        }
        return null;
    }
}
