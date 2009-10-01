package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.Source;
import com.seovic.coherence.loader.Target;

import com.seovic.core.Updater;
import com.seovic.core.updater.MapUpdater;

import java.io.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * A {@link Target} implementation that writes items into a CSV file.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public class XmlTarget
        extends AbstractBaseTarget
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct XmlTarget instance.
     *
     * @param writer           writer to use
     * @param rootElementName  root element name
     * @param itemElementName  item element name
     * @param propertyNames    property names (any property prefixed with '@'
     *                         will be written out as attribute)
     */
    public XmlTarget(Writer writer,
                     String rootElementName,
                     String itemElementName,
                     String propertyNames)
        {
        this(writer, null, rootElementName, itemElementName, propertyNames);
        }

    /**
     * Construct XmlTarget instance.
     *
     * @param writer           writer to use
     * @param rootElementName  root element name
     * @param itemElementName  item element name
     * @param propertyNames    property names (any property prefixed with '@'
     *                         will be written out as attribute)
     */
    public XmlTarget(Writer writer,
                     String rootElementName,
                     String itemElementName,
                     String... propertyNames)
        {
        this(writer, null, rootElementName, itemElementName, propertyNames);
        }

    /**
     * Construct XmlTarget instance.
     *
     * @param writer           writer to use
     * @param namespaces       namespace map
     * @param rootElementName  root element name
     * @param itemElementName  item element name
     * @param propertyNames    property names (any property prefixed with '@'
     *                         will be written out as attribute)
     */
    public XmlTarget(Writer writer,
                     Map<String, String> namespaces,
                     String rootElementName,
                     String itemElementName,
                     String propertyNames)
        {
        this(writer, namespaces, rootElementName, itemElementName,
             propertyNames.split(","));
        }

    /**
     * Construct XmlTarget instance.
     *
     * @param writer           writer to use
     * @param namespaces       namespace map
     * @param rootElementName  root element name
     * @param itemElementName  item element name
     * @param propertyNames    property names (any property prefixed with '@'
     *                         will be written out as attribute)
     */
    public XmlTarget(Writer writer,
                     Map<String, String> namespaces,
                     String rootElementName,
                     String itemElementName,
                     String... propertyNames)
        {
        try
            {
            this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    writer);
            this.namespacesMap = namespaces == null
                                 ? new HashMap<String, String>()
                                 : namespaces;
            this.rootElementName = rootElementName;
            this.itemElementName = itemElementName;
            initAttributesAndElements(propertyNames);
            }
        catch (XMLStreamException e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- AbstractBaseTarget implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Updater createDefaultUpdater(String propertyName)
        {
        return new MapUpdater(propertyName);
        }


    // ---- Source implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginImport()
        {
        try
            {
            writer.writeStartDocument();
            writer.writeStartElement(rootElementName);
            for (Map.Entry<String, String> entry : namespacesMap.entrySet())
                {
                writer.writeNamespace(entry.getKey(), entry.getValue());
                }
            }
        catch (XMLStreamException e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked"})
    public void importItem(Object item)
        {
        try
            {
            boolean hasDecendants = !elements.isEmpty();
            if (hasDecendants)
                {
                writer.writeStartElement(itemElementName);
                }
            else
                {
                writer.writeEmptyElement(itemElementName);
                }
            Map<String, Object> convertedItem = (Map<String, Object>) item;
            for (Property property : attributes)
                {
                String prefix = property.getNamespacePrefix();
                if (prefix == null)
                    {
                    prefix = "";
                    }
                String localName = property.getLocalName();
                String namespace = namespacesMap.get(prefix);
                if (namespace == null)
                    {
                    namespace = "";
                    }
                writer.writeAttribute(prefix,
                                      namespace,
                                      localName,
                                      convertedItem.get(localName).toString());
                }
            for (Property property : elements)
                {
                String prefix = property.getNamespacePrefix() == null
                                ? ""
                                : property.getNamespacePrefix();
                String localName = property.getLocalName();
                String namespace = namespacesMap.get(prefix) == null
                                   ? ""
                                   : namespacesMap.get(prefix);
                writer.writeStartElement(prefix, localName, namespace);
                writer.writeCharacters(convertedItem.get(localName).toString());
                writer.writeEndElement();
                }
            if (hasDecendants)
                {
                writer.writeEndElement();
                }
            }
        catch (XMLStreamException e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endImport()
        {
        try
            {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
            }
        catch (XMLStreamException e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames()
        {
        String[] propertyNames =
                new String[attributes.size() + elements.size()];
        int i = 0;
        for (Property property : attributes)
            {
            propertyNames[i++] = property.getLocalName();
            }
        for (Property property : elements)
            {
            propertyNames[i++] = property.getLocalName();
            }
        return propertyNames;
        }

    /**
     * {@inheritDoc}
     */
    public Object createTargetInstance(Source source, Object sourceItem)
        {
        return new HashMap<String, Object>();
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Parses user-specified property names and determines which properties
     * should be written out as attributes and which as child elements.
     *
     * @param propertyNames  property names
     */
    private void initAttributesAndElements(String... propertyNames)
        {
        for (String propertyName : propertyNames)
            {
            Property property = new Property(propertyName);
            if (property.isAttribute())
                {
                attributes.add(property);
                }
            else
                {
                elements.add(property);
                }
            }
        }

    // ---- inner class: Property -------------------------------------------

    /**
     * Represents a single property.
     */
    private static class Property
        {
        // ---- constructors --------------------------------------------

        /**
         * Create property instance.
         *
         * @param propertyExpr  property expression to parse
         */
        public Property(String propertyExpr)
            {
            Matcher matcher = PATTERN.matcher(propertyExpr);
            if (matcher.matches())
                {
                m_nsPrefix = matcher.group(2);
                m_fAttribute = matcher.group(3) != null;
                m_localName = matcher.group(4);
                }
            else
                {
                throw new IllegalArgumentException(
                        "Property name must be in the [ns:][@]name format.");
                }
            }

        /**
         * Return namespace prefix.
         *
         * @return namespace prefix
         */
        public String getNamespacePrefix()
            {
            return m_nsPrefix;
            }

        /**
         * Return true if this property should be written out as attribute.
         *
         * @return true if this property should be written out as attribute
         */
        public boolean isAttribute()
            {
            return m_fAttribute;
            }

        /**
         * Return local name
         *
         * @return local name
         */
        public String getLocalName()
            {
            return m_localName;
            }

        // ---- Object methods ----------------------------------------------

        /**
         * Test objects for equality.
         *
         * @param o  object to compare this object with
         *
         * @return <tt>true</tt> if the specified object is equal to this object
         *         <tt>false</tt> otherwise
         */
        @Override
        public boolean equals(Object o)
            {
            if (this == o)
                {
                return true;
                }
            if (o == null || getClass() != o.getClass())
                {
                return false;
                }

            Property property = (Property) o;

            return m_fAttribute == property.m_fAttribute
                   && m_localName.equals(property.m_localName)
                   && m_nsPrefix == null
                       ? property.m_nsPrefix == null
                       : m_nsPrefix.equals(property.m_nsPrefix);
            }

        /**
         * Return hash code for this object.
         *
         * @return this object's hash code
         */
        @Override
        public int hashCode()
            {
            int result = m_nsPrefix != null ? m_nsPrefix.hashCode() : 0;
            result = 31 * result + (m_fAttribute ? 1 : 0);
            result = 31 * result + m_localName.hashCode();
            return result;
            }

        // ---- data members --------------------------------------------

        /**
         * Regex expression to use when parsing property names.
         */
        private static final Pattern PATTERN =
                Pattern.compile("((.*):)?(@)?(.+)");

        /**
         * Namespace prefix.
         */
        private String m_nsPrefix;

        /**
         * True if this property shold be writtin out as attribute.
         */
        private boolean m_fAttribute;

        /**
         * Property name.
         */
        private String m_localName;
        }


    // ---- data members ----------------------------------------------------

    /**
     * A writer to use.
     */
    private XMLStreamWriter writer;

    /**
     * Root element name.
     */
    private String rootElementName;

    /**
     * Item element name.
     */
    private String itemElementName;

    /**
     * A list of property names that should be written as attributes.
     */
    private List<Property> attributes = new ArrayList<Property>();

    /**
     * A list of property names that should be written as child elements.
     */
    private List<Property> elements = new ArrayList<Property>();

    /**
     * Namespace map.
     */
    private Map<String, String> namespacesMap;
    }
