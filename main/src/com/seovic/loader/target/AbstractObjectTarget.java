package com.seovic.loader.target;


import com.seovic.core.Updater;
import com.seovic.core.Defaults;
import com.seovic.loader.Source;
import com.seovic.loader.Target;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


/**
 * Abstract base class for {@link Target} implementations that expect instances
 * of a specific item class as arguments to {@link #importItem(Object)} method.
 *
 * @author Aleksandar Seovic  2012.04.06
 */
public abstract class AbstractObjectTarget
        extends AbstractBaseTarget {
    /**
     * The class of objects to load.
     */
    private Class m_itemClass;
    
    /**
     * Target item constructor.
     */
    private transient Constructor m_itemCtor;

    /**
     * Construct AbstractObjectTarget instance.
     *
     * @param itemClass  class of the items to load
     */
    protected AbstractObjectTarget(Class itemClass) {
        m_itemClass = itemClass;
    }

    /**
     * {@inheritDoc}
     */
    protected Updater createDefaultUpdater(String propertyName) {
        return Defaults.createUpdater(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public abstract void importItem(Object item);

    /**
     * {@inheritDoc}
     */
    public Set<String> getPropertyNames() {
        List<PropertyDescriptor> properties = getWriteableProperties(m_itemClass);

        Set<String> propertyNames = new HashSet<String>(properties.size());
        for (PropertyDescriptor pd : properties) {
            propertyNames.add(pd.getName());
        }

        return propertyNames;
    }

    /**
     * {@inheritDoc}
     */
    public Object createTargetInstance(Source source, Object sourceItem) {
        try {
            if (m_itemCtor == null) {
                m_itemCtor = m_itemClass.getConstructor();
            }
            return m_itemCtor.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
