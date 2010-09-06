package com.seovic.util;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

import org.springframework.util.Assert;


/**
 * @author Aleksandar Seovic  2010.08.18
 */
public abstract class ReflectionUtils {
    public static Map<String, Object> getPropertyMap(Object obj) {
        Assert.notNull(obj, "Argument cannot be null");

        try {
            BeanInfo             beanInfo            = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            Map<String, Object>  propertyMap         = new HashMap<String, Object>(propertyDescriptors.length);
            for (PropertyDescriptor pd : propertyDescriptors) {
                Method getter = pd.getReadMethod();
                if (getter != null) {
                    propertyMap.put(pd.getName(), getter.invoke(obj));
                }
            }
            return propertyMap;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
