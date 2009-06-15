package com.seovic.coherence.loader.properties;


import com.seovic.coherence.loader.PropertyGetter;
import java.util.Map;


/**
 * @author Aleksandar Seovic  2009.06.15
 */
public class MapPropertyGetter
        implements PropertyGetter {
    private String propertyName;

    public MapPropertyGetter(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue(Object sourceItem) {
        return ((Map) sourceItem).get(propertyName);
    }
}
