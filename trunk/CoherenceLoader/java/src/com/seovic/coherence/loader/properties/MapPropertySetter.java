package com.seovic.coherence.loader.properties;


import com.seovic.coherence.loader.PropertySetter;

import java.util.Map;


/**
 * @author Aleksandar Seovic  2009.06.15
 */
public class MapPropertySetter
        implements PropertySetter {
    private String propertyName;

    public MapPropertySetter(String propertyName) {
        this.propertyName = propertyName;
    }

    @SuppressWarnings({"unchecked"})
    public void setValue(Object targetItem, Object value) {
        ((Map) targetItem).put(propertyName, value);
    }
}