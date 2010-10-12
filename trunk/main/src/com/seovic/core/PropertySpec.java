package com.seovic.core;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;
import java.io.Serializable;


/**
 * @author Aleksandar Seovic  2010.10.11
 */
public class PropertySpec implements Serializable, PortableObject {
    private String m_name;
    private PropertyList m_propertyList;

    public PropertySpec() {
    }

    public PropertySpec(String name) {
        this(name, null);
    }

    public PropertySpec(String name, PropertyList propertyList) {
        m_name = name;
        m_propertyList = propertyList;
    }

    public static PropertySpec fromString(String propertySpec) {
        return PropertyList.fromString(propertySpec).first();    
    }

    public String getName() {
        return m_name;
    }

    public PropertyList getPropertyList() {
        return m_propertyList;
    }

    @Override
    public void readExternal(PofReader reader)
            throws IOException {
        m_name = reader.readString(0);
        m_propertyList = (PropertyList) reader.readObject(1);
    }

    @Override
    public void writeExternal(PofWriter writer)
            throws IOException {
        writer.writeString(0, m_name);
        writer.writeObject(1, m_propertyList);
    }
}
