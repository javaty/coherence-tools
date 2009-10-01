package com.seovic.coherence.loader.source;


import com.seovic.core.Extractor;
import com.seovic.core.extractor.XmlExtractor;
import com.seovic.coherence.loader.Source;

import java.io.Reader;
import java.io.StringReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.InputSource;


/**
 * A {@link Source} implementation that reads items to load from an XML file.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.18
 */
public class XmlSource
        extends AbstractBaseSource
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct a CsvSource instance.
     *
     * @param reader  reader to use to read CSV file with
     */
    public XmlSource(Reader reader)
        {
        try
            {
            this.reader = XMLInputFactory.newInstance().createXMLStreamReader(
                    reader);
            }
        catch (XMLStreamException e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- Iterable implementation -----------------------------------------

    /**
     * Return an iterator over this source.
     *
     * @return a source iterator
     */
    public Iterator iterator()
        {
        return new XmlIterator(reader);
        }


    // ---- AbstractBaseSource implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Extractor createDefaultExtractor(String propertyName)
        {
        return new XmlExtractor(propertyName);
        }


    // ---- inner class: XmlIterator ----------------------------------------

    /**
     * Iterator implementation for XmlSource.
     */
    public static class XmlIterator
            implements Iterator
        {
        // ---- constructors --------------------------------------------

        /**
         * Construct XmlIterator instance.
         *
         * @param reader  reader to use
         */
        public XmlIterator(XMLStreamReader reader)
            {
            this.documentFactory = DocumentBuilderFactory.newInstance();
            this.documentFactory.setNamespaceAware(true);
            this.reader = reader;
            // skip document element
            try
                {
                int eventType = reader.next();
                while (eventType != XMLStreamConstants.START_ELEMENT)
                    {
                    eventType = reader.next();
                    }
                int count = reader.getNamespaceCount();
                for (int i = 0; i < count; i++)
                    {
                    String prefix = reader.getNamespacePrefix(i);
                    String nsUri = reader.getNamespaceURI(i);
                    namespaceMap.put(prefix, nsUri);
                    }
                }
            catch (XMLStreamException e)
                {
                throw new RuntimeException(e);
                }
            }

        // ---- Iterator implementation ---------------------------------

        /**
         * Returns true if there are more items to read, false otherwise.
         *
         * @return true if there are more items to read, false otherwise
         */
        public boolean hasNext()
            {
            try
                {
                int eventType = reader.next();
                while (eventType != XMLStreamConstants.START_ELEMENT
                       && eventType != XMLStreamConstants.END_DOCUMENT)
                    {
                    eventType = reader.next();
                    }
                return eventType == XMLStreamConstants.START_ELEMENT;
                }
            catch (XMLStreamException e)
                {
                throw new RuntimeException(e);
                }
            }

        /**
         * Reads the next item from the file and returns it as XmlDocument.
         *
         * @return an XmlDocument representing next item in the file
         */
        public Object next()
            {
            try
                {
                String elementStr = getNextElement(reader);
                return documentFactory
                        .newDocumentBuilder()
                        .parse(new InputSource(new StringReader(elementStr)));
                }
            catch (Exception e)
                {
                throw new RuntimeException(e);
                }
            }

        /**
         * Not supported.
         */
        public void remove()
            {
            throw new UnsupportedOperationException(
                    "XmlFileIterator supports " +
                    " only read operations");
            }

        // ---- helper methods ------------------------------------------

        /**
         * Reads the next element from the stream.
         *
         * @param reader  reader to use
         *
         * @return element as a string
         *
         * @throws XMLStreamException  if an error occurs
         */
        protected String getNextElement(XMLStreamReader reader)
                throws XMLStreamException
            {
            while (reader.getEventType() != XMLStreamConstants.START_ELEMENT
                   && reader.hasNext())
                {
                reader.next();
                }
            StringBuilder content = new StringBuilder();
            writeElement(content, namespaceMap);
            return content.toString();
            }

        /**
         * Writes properly namespaced element into the specified string builder.
         *
         * @param content  string builder to write element into
         * @param nsMap    namespace map
         *
         * @throws XMLStreamException  if an error occurs
         */
        protected void writeElement(StringBuilder content,
                                  Map<String, String> nsMap)
                throws XMLStreamException
            {
            QName elementName = reader.getName();

            writeOpeningTag(content, elementName);
            if (nsMap != null)
                {
                writeNamespaces(content, nsMap);
                }
            writeAttributes(content);
            writeContents(content);
            writeClosingTag(content, elementName);
            }

        /**
         * Writes opening tag into the specified string builder.
         *
         * @param content      string builder to write element into
         * @param elementName  element name
         */
        protected void writeOpeningTag(StringBuilder content, QName elementName)
            {
            String prefix = elementName.getPrefix();
            content.append("<");
            if (prefix != null && prefix.length() > 0)
                {
                content.append(prefix).append(":");
                }
            content.append(elementName.getLocalPart());
            }

        /**
         * Writes namespace declarations into the specified string builder.
         *
         * @param content  string builder to write element into
         * @param nsMap    namespace map
         */
        protected void writeNamespaces(StringBuilder content,
                                       Map<String, String> nsMap)
            {
            String prefix;
            for (Map.Entry<String, String> ns : nsMap.entrySet())
                {
                prefix = ns.getKey();
                String nsUri = ns.getValue();
                content.append(" xmlns");
                if (prefix != null && prefix.length() > 0)
                    {
                    content.append(":").append(prefix);
                    }
                content.append("=\"")
                        .append(nsUri)
                        .append("\"");
                }
            }

        /**
         * Writes attributes into the specified string builder.
         *
         * @param content  string builder to write element into
         */
        protected void writeAttributes(StringBuilder content)
            {
            String prefix;// write attributes
            int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++)
                {
                QName name = reader.getAttributeName(i);
                String value = reader.getAttributeValue(i);
                prefix = name.getPrefix();
                content.append(" ");
                if (prefix != null && prefix.length() > 0)
                    {
                    content.append(prefix).append(":");
                    }
                content.append(name.getLocalPart())
                        .append("=\"")
                        .append(value)
                        .append("\"");
                }
            content.append(">");
            }

        /**
         * Writes element contents into the specified string builder.
         *
         * @param content  string builder to write element into
         *
         * @throws XMLStreamException  if an error occurs
         */
        protected void writeContents(StringBuilder content)
                throws XMLStreamException
            {
            int eventType = reader.getEventType();
            // write children
            while (eventType != XMLStreamConstants.END_ELEMENT)
                {
                eventType = reader.next();
                if (eventType == XMLStreamConstants.CHARACTERS
                    || eventType == XMLStreamConstants.CDATA
                    || eventType == XMLStreamConstants.SPACE
                    || eventType == XMLStreamConstants.ENTITY_REFERENCE)
                    {
                    content.append(reader.getText());
                    }
                else if (eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                         || eventType == XMLStreamConstants.COMMENT)
                    {
                    // skipping
                    }
                else if (eventType == XMLStreamConstants.END_DOCUMENT)
                        {
                        throw new XMLStreamException(
                                "unexpected end of document when reading element text content",
                                reader.getLocation());
                        }
                    else if (eventType == XMLStreamConstants.START_ELEMENT)
                            {
                            writeElement(content, null);
                            }
                }
            }

        /**
         * Writes closing tag into the specified string builder.
         *
         * @param content      string builder to write element into
         * @param elementName  element name
         */
        protected void writeClosingTag(StringBuilder content, QName elementName)
            {
            String prefix = elementName.getPrefix();
            content.append("</");
            if (prefix != null && prefix.length() > 0)
                {
                content.append(prefix).append(":");
                }
            content.append(elementName.getLocalPart()).append(">");
            }

        // ---- data members --------------------------------------------

        /**
         * Reader to use.
         */
        private XMLStreamReader reader;

        /**
         * Namespace map.
         */
        private Map<String, String> namespaceMap =
                new HashMap<String, String>();

        /**
         * Document builder factory.
         */
        private DocumentBuilderFactory documentFactory;
        }

    /**
     * Reader to use.
     */
    private XMLStreamReader reader;
    }
