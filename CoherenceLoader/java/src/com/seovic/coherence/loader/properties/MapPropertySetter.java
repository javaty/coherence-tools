package com.seovic.coherence.loader.properties;


import com.seovic.coherence.loader.PropertySetter;
import java.util.Map;


/**
 * @author Aleksandar Seovic  2009.06.15
 */
public class MapPropertySetter
        implements PropertySetter
    {
    private String propertyName;

    public MapPropertySetter(String propertyName)
        {
        if (propertyName == null)
            {
            throw new IllegalArgumentException("Property name cannot be null");
            }
        this.propertyName = propertyName;
        }

    @SuppressWarnings({"unchecked"})
    public void setValue(Object targetItem, Object value)
        {
        if (targetItem == null)
            {
            throw new IllegalArgumentException("Target object to set the property [" + propertyName + "] to, cannot be null");
            }
        ((Map) targetItem).put(propertyName, value);
        }
    }