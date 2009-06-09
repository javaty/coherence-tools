package com.seovic.coherence.loader.target;

import com.seovic.coherence.loader.Target;
import com.seovic.coherence.loader.PropertyMapper;
import com.seovic.coherence.loader.mapper.ExpressionPropertyMapper;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public abstract class AbstractBaseTarget implements Target {
    private Map<String, PropertyMapper> propertyMappers = new HashMap<String, PropertyMapper>();

    protected AbstractBaseTarget() {
    }

    public void registerPropertyMapper(String targetProperty, PropertyMapper mapper) {
        propertyMappers.put(targetProperty, mapper);
    }

    public void beginImport() {
        // default implementation
    }

    public void endImport() {
        // default implementation
    }

    protected PropertyMapper getPropertyMapper(String propertyName) {
        PropertyMapper mapper = propertyMappers.get(propertyName);
        if (mapper == null) {
            mapper = new ExpressionPropertyMapper(propertyName);
        }
        return mapper;
    }

    protected static List<PropertyDescriptor> getWriteableProperties(Class cls) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(cls);
        List<PropertyDescriptor> writeableProperties = new ArrayList<PropertyDescriptor>();
        PropertyDescriptor[] props = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            if (isWriteable(prop)) {
                writeableProperties.add(prop);
            }
        }
        return writeableProperties;
    }

    protected static List<PropertyDescriptor> getReadableProperties(Class cls) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(cls);
        List<PropertyDescriptor> readableProperties = new ArrayList<PropertyDescriptor>();
        PropertyDescriptor[] props = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            if (isReadable(prop)) {
                readableProperties.add(prop);
            }
        }
        return readableProperties;
    }

    private static boolean isReadable(PropertyDescriptor property) {
        return property.getReadMethod() != null;
    }

    private static boolean isWriteable(PropertyDescriptor property) {
        return property.getWriteMethod() != null;
    }

}