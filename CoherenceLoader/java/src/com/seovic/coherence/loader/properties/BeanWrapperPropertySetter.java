package com.seovic.coherence.loader.properties;


import com.seovic.coherence.loader.PropertySetter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


/**
 * @author Aleksandar Seovic  2009.06.15
 */
public class BeanWrapperPropertySetter implements PropertySetter {
    private String propertyName;

    public BeanWrapperPropertySetter(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setValue(Object targetItem, Object value) {
        BeanWrapper bw = new BeanWrapperImpl(targetItem);
        bw.setPropertyValue(propertyName, value);
    }
}
