package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.Target;

import com.seovic.core.Updater;

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
    private Map<String, Updater> updaters =
            new HashMap<String, Updater>();

    protected AbstractBaseTarget()
    {
    }

    protected abstract Updater createDefaultUpdater(String propertyName);

    public void beginImport()
        {
        // default implementation
        }

    public void endImport()
        {
        // default implementation
        }

    public Updater getUpdater(String propertyName)
        {
        Updater updater = updaters.get(propertyName);
        return updater != null
               ? updater
               : createDefaultUpdater(propertyName);
        }

    public void setUpdater(String targetProperty, Updater updater)
        {
        updaters.put(targetProperty, updater);
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