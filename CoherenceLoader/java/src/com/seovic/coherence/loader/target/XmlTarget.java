package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.PropertySetter;
import com.seovic.coherence.loader.properties.MapPropertySetter;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import java.io.Writer;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author ic  2009.06.10
 */
public class XmlTarget extends AbstractBaseTarget {
    private XMLStreamWriter     writer;
    private String              rootElementName;
    private String              itemElementName;
    private List<Property>      attributes = new ArrayList<Property>();
    private List<Property>      elements   = new ArrayList<Property>();
    private Map<String, String> namespacesMap;

    public XmlTarget(Writer writer,
                     String rootElementName,
                     String itemElementName,
                     String propertyNames) {
        this(writer, null, rootElementName, itemElementName, propertyNames);
    }

    public XmlTarget(Writer writer,
                     String rootElementName,
                     String itemElementName,
                     String... propertyNames) {
        this(writer, null, rootElementName, itemElementName, propertyNames);
    }

    public XmlTarget(Writer writer,
                     Map<String, String> namespaces,
                     String rootElementName,
                     String itemElementName,
                     String propertyNames) {
        this(writer, namespaces, rootElementName, itemElementName, propertyNames.split(","));
    }

    public XmlTarget(Writer writer,
                     Map<String, String> namespaces,
                     String rootElementName,
                     String itemElementName,
                     String... propertyNames) {
        try {
            this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
            this.namespacesMap   = namespaces == null
                                    ? new HashMap<String, String>()
                                    : namespaces;
            this.rootElementName = rootElementName;
            this.itemElementName = itemElementName;
            initAttributesAndElements(propertyNames);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    protected PropertySetter createDefaultSetter(String propertyName) {
        return new MapPropertySetter(propertyName);
    }

    @Override
    public void beginImport() {
        try {
            writer.writeStartDocument();
            writer.writeStartElement(rootElementName);
            for (Map.Entry<String, String> entry : namespacesMap.entrySet()) {
                writer.writeNamespace(entry.getKey(), entry.getValue());
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public void importItem(Object item) {
        try {
            boolean hasDecendants = !elements.isEmpty();
            if (hasDecendants) {
                writer.writeStartElement(itemElementName);                
            } else {
                writer.writeEmptyElement(itemElementName);
            }
            Map<String, Object> convertedItem = (Map<String, Object>) item;
            for (Property property : attributes) {
                String prefix = property.getNamespacePrefix();
                if (prefix == null) {
                    prefix = "";
                }
                String localName = property.getLocalName();
                String namespace = namespacesMap.get(prefix);
                if (namespace == null) {
                    namespace = "";
                }
                writer.writeAttribute(prefix,
                                      namespace,
                                      localName,
                                      convertedItem.get(localName).toString());
            }
            for (Property property : elements) {
                String prefix = property.getNamespacePrefix() == null ? "" : property.getNamespacePrefix();
                String localName = property.getLocalName();
                String namespace = namespacesMap.get(prefix) == null ? "" : namespacesMap.get(prefix);
                writer.writeStartElement(prefix, localName, namespace);
                writer.writeCharacters(convertedItem.get(localName).toString());
                writer.writeEndElement();
            }
            if (hasDecendants) {
                writer.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endImport() {
        try {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getPropertyNames() {
        String[] propertyNames = new String[attributes.size() + elements.size()];
        int i = 0;
        for(Property property : attributes) {
            propertyNames[i++] = property.getLocalName();
        }
        for(Property property : elements) {
            propertyNames[i++] = property.getLocalName();
        }
        return propertyNames;
    }

    public Object createTargetInstance() {
        return new HashMap<String, Object>();
    }

    private void initAttributesAndElements(String... propertyNames) {
        for (String propertyName : propertyNames) {
            Property property = new Property(propertyName);
            if (property.isAttribute()) {
                attributes.add(property);
            } else {
                elements.add(property);
            }
        }
    }

    private static class Property {
        private static final Pattern PATTERN = Pattern.compile("((.*):)?(@)?(.+)");

        private String  nsPrefix;
        private boolean isAttribute;
        private String  localName;

        public Property(String propertyExpr) {
            Matcher matcher = PATTERN.matcher(propertyExpr);
            if (matcher.matches()) {
                nsPrefix    = matcher.group(2);
                isAttribute = matcher.group(3) != null;
                localName   = matcher.group(4);
            }
            else {
                throw new IllegalArgumentException("Property name must be in the [ns:][@]name format.");
            }
        }

        public String getNamespacePrefix() {
            return nsPrefix;
        }

        public boolean isAttribute() {
            return isAttribute;
        }

        public String getLocalName() {
            return localName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Property property = (Property) o;

            return isAttribute == property.isAttribute
                    && localName.equals(property.localName)
                    && !(nsPrefix != null
                        ? !nsPrefix.equals(property.nsPrefix)
                        : property.nsPrefix != null);
        }

        @Override
        public int hashCode() {
            int result = nsPrefix != null ? nsPrefix.hashCode() : 0;
            result = 31 * result + (isAttribute ? 1 : 0);
            result = 31 * result + localName.hashCode();
            return result;
        }
    }
}
