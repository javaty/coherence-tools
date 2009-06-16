package com.seovic.coherence.loader.source;


import com.seovic.coherence.loader.PropertyGetter;
import com.seovic.coherence.loader.Source;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Aleksandar Seovic  2009.06.15
 */
public abstract class AbstractBaseSource
        implements Source
    {
    private Map<String, PropertyGetter> propertyGetters;

    protected AbstractBaseSource()
        {
        propertyGetters = new HashMap<String, PropertyGetter>();
        }

    protected abstract PropertyGetter createDefaultGetter(String propertyName);

    public PropertyGetter getPropertyGetter(String propertyName)
        {
        PropertyGetter getter = propertyGetters.get(propertyName);
        return getter != null
               ? getter
               : createDefaultGetter(propertyName);
        }

    public void setPropertyGetter(String propertyName,
                                  PropertyGetter propertyGetter)
        {
        propertyGetters.put(propertyName, propertyGetter);
        }
    }
