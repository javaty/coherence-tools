package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.PropertySetter;
import com.seovic.coherence.loader.Target;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public abstract class AbstractBaseTarget
        implements Target
    {
    private Map<String, PropertySetter> propertySetters =
            new HashMap<String, PropertySetter>();

    protected AbstractBaseTarget()
        {
        }

    protected abstract PropertySetter createDefaultSetter(String propertyName);

    public void beginImport()
        {
        // default implementation
        }

    public void endImport()
        {
        // default implementation
        }

    public PropertySetter getPropertySetter(String propertyName)
        {
        PropertySetter setter = propertySetters.get(propertyName);
        return setter != null
               ? setter
               : createDefaultSetter(propertyName);
        }

    public void setPropertySetter(String targetProperty, PropertySetter mapper)
        {
        propertySetters.put(targetProperty, mapper);
        }

    protected static List<PropertyDescriptor> getWriteableProperties(Class cls)
        {
        BeanWrapper beanWrapper = new BeanWrapperImpl(cls);
        List<PropertyDescriptor> writeableProperties =
                new ArrayList<PropertyDescriptor>();
        PropertyDescriptor[] props = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor prop : props)
            {
            if (isWriteable(prop))
                {
                writeableProperties.add(prop);
                }
            }
        return writeableProperties;
        }

    private static boolean isWriteable(PropertyDescriptor property)
        {
        return property.getWriteMethod() != null;
        }
    }