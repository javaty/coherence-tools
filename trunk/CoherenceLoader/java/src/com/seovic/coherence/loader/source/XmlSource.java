package com.seovic.coherence.loader.source;


import com.seovic.coherence.loader.PropertyGetter;
import com.seovic.coherence.loader.properties.XmlPropertyGetter;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import java.io.Reader;
import java.io.StringReader;

import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;

import javax.xml.namespace.QName;


/**
 * @author ic  2009.06.10
 */
public class XmlSource extends AbstractBaseSource {
    private XMLStreamReader reader;


    public XmlSource(Reader reader) {
        try {
            this.reader = XMLInputFactory.newInstance().createXMLStreamReader(reader);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterator iterator() {
        return new XmlIterator(reader);
    }

    protected PropertyGetter createDefaultGetter(String propertyName) {
        return new XmlPropertyGetter(propertyName);
    }

    public static class XmlIterator implements Iterator {
        private XMLStreamReader reader;
        private Map<String, String> namespaceMap = new HashMap<String, String>();

        public XmlIterator(XMLStreamReader reader) {
            this.reader = reader;
            // skip document element
            try {
                int eventType = reader.next();
                while (eventType != XMLStreamConstants.START_ELEMENT) {
                    eventType = reader.next();
                }
                int count = reader.getNamespaceCount();
                for (int i = 0; i < count; i++) {
                    String prefix = reader.getNamespacePrefix(i);
                    String nsUri  = reader.getNamespaceURI(i);
                    namespaceMap.put(prefix, nsUri);
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            try {
                int eventType = reader.next();
                while (eventType != XMLStreamConstants.START_ELEMENT
                        && eventType != XMLStreamConstants.END_DOCUMENT) {
                    eventType = reader.next();
                }
                return eventType == XMLStreamConstants.START_ELEMENT;
            }
            catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }

        public Object next() {
            try {
                String elementStr = getNextElement(reader);
                return DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(new InputSource(new StringReader(elementStr)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private String getNextElement(XMLStreamReader reader) throws XMLStreamException {
            while (reader.getEventType() != XMLStreamConstants.START_ELEMENT && reader.hasNext()) {
                reader.next();
            }
            StringBuilder content = new StringBuilder();
            writeElement(content, namespaceMap);
            return content.toString();
        }

        public void remove() {
            throw new UnsupportedOperationException("XmlFileIterator supports " +
                    " only read operations");
        }

        private void writeElement(StringBuilder content, Map<String, String> nsMap) throws XMLStreamException {
            QName elementName = reader.getName();

            writeOpeningTag(content, elementName);
            if (nsMap != null) {
                writeNamespaces(content, nsMap);
            }
            writeAttributes(content);
            writeContents(content);
            writeClosingTag(content, elementName);
        }

        protected void writeOpeningTag(StringBuilder content, QName elementName) {
            String prefix = elementName.getPrefix();
            content.append("<");
            if (prefix != null && prefix.length() > 0) {
                content.append(prefix).append(":");
            }
            content.append(elementName.getLocalPart());
        }

        protected void writeNamespaces(StringBuilder content, Map<String, String> nsMap) {
            String prefix;
            for (Map.Entry<String, String> ns : nsMap.entrySet()) {
                prefix = ns.getKey();
                String nsUri  = ns.getValue();
                content.append(" xmlns");
                if (prefix != null && prefix.length() > 0) {
                    content.append(":").append(prefix);
                }
                content.append("=\"")
                        .append(nsUri)
                        .append("\"");
            }
        }

        protected void writeAttributes(StringBuilder content) {
            String prefix;// write attributes
            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                QName name = reader.getAttributeName(i);
                String value = reader.getAttributeValue(i);
                prefix = name.getPrefix();
                content.append(" ");
                if (prefix != null && prefix.length() > 0) {
                    content.append(prefix).append(":");
                }
                content.append(name.getLocalPart())
                        .append("=\"")
                        .append(value)
                        .append("\"");
            }
            content.append(">");
        }

        protected void writeContents(StringBuilder content) throws XMLStreamException {
            int eventType = reader.getEventType();
            // write children
            while (eventType != XMLStreamConstants.END_ELEMENT) {
                eventType = reader.next();
                if (eventType == XMLStreamConstants.CHARACTERS
                        || eventType == XMLStreamConstants.CDATA
                        || eventType == XMLStreamConstants.SPACE
                        || eventType == XMLStreamConstants.ENTITY_REFERENCE) {
                    content.append(reader.getText());
                } else if (eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                        || eventType == XMLStreamConstants.COMMENT) {
                    // skipping
                } else if (eventType == XMLStreamConstants.END_DOCUMENT) {
                    throw new XMLStreamException("unexpected end of document when reading element text content", reader.getLocation());
                } else if (eventType == XMLStreamConstants.START_ELEMENT) {
                    writeElement(content, null);
                }
            }
        }

        protected void writeClosingTag(StringBuilder content, QName elementName) {
            String prefix = elementName.getPrefix();
            content.append("</");
            if (prefix != null && prefix.length() > 0) {
                content.append(prefix).append(":");
            }
            content.append(elementName.getLocalPart()).append(">");
        }
    }
}
