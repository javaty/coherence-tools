package com.seovic.core;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;
import java.io.IOException;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;


/**
 * @author Aleksandar Seovic  2010.10.11
 */
public class PropertyList implements Iterable<PropertySpec>, Serializable, PortableObject {
    private List<PropertySpec> m_properties;

    public PropertyList() {
        m_properties = new LinkedList<PropertySpec>();
    }

    public PropertyList(PropertySpec... properties) {
        this(Arrays.asList(properties));
    }

    public PropertyList(List<PropertySpec> properties) {
        m_properties = properties;
    }

    public static PropertyList fromString(String sPropertyList) {
        PropertyList properties = new PropertyList();
        sPropertyList = sPropertyList.trim();

        StringBuilder name = new StringBuilder();
        PropertyList list = null;

        int pos = 0;
        while (pos < sPropertyList.length()) {
            char c = sPropertyList.charAt(pos++);
            if (c == ',') {
                // terminate property
                properties.add(new PropertySpec(name.toString(), list));
                name = new StringBuilder();
                list = null;
            }
            else if (c == ':') {
                // property has inner property list
                pos++; // skip first open parenthesis
                int parenCount = 1;
                StringBuilder innerList = new StringBuilder();
                while (parenCount > 0 && pos < sPropertyList.length()) {
                    char s = sPropertyList.charAt(pos++);
                    if (s == ')') {
                        parenCount--;
                    }
                    else if (s == '(') {
                        parenCount++;
                    }

                    if (parenCount > 0) {
                        innerList.append(s);
                    }
                }

                list = fromString(innerList.toString());
            }
            else if (c != ' ') {
                name.append(c);
            }
        }

        if (name.length() > 0) {
            properties.add(new PropertySpec(name.toString(), list));
        }

        return properties;
    }

    public void add(PropertySpec property) {
        m_properties.add(property);
    }

    List<PropertySpec> getProperties() {
        return m_properties;
    }

    PropertySpec first() {
        if (m_properties.size() > 0) {
            return m_properties.get(0);
        }
        return null;
    }

    @Override
    public Iterator<PropertySpec> iterator() {
        return m_properties.iterator();
    }

    @Override
    public void readExternal(PofReader reader)
            throws IOException {
        reader.readCollection(0, m_properties);
    }

    @Override
    public void writeExternal(PofWriter writer)
            throws IOException {
        writer.writeCollection(0, m_properties);
    }
}
