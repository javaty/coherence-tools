package com.seovic.coherence;


import com.seovic.core.Entity;
import com.seovic.core.Updatable;
import com.seovic.core.Updater;
import com.seovic.core.DynamicObject;
import com.seovic.core.updater.PropertyUpdater;
import com.seovic.core.updater.DynamicObjectUpdater;
import com.seovic.coherence.util.AbstractCoherenceRepository;

import com.tangosol.io.AbstractEvolvable;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;


/**
 * Base class for entities that are store in Coherence.
 * <p/>
 * This class provides several convenience methods that allow you to execute
 * mutating operations in-place, within the cluster.
 *
 * @author Aleksandar Seovic  2010.11.20
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractCoherenceEntity<T>
        extends AbstractEvolvable
        implements Entity<T>, Updatable {

    protected T m_id;

    /**
     * Return the repository implementation for this entity.
     *
     * @return this entity's repository
     */
    protected abstract AbstractCoherenceRepository<T, ? extends Entity<T>> getRepository();

    /**
     * Set value of a property both on the local instance and on the remote
     * master object.
     *
     * @param propertyName  the name of the property to update
     * @param value         new property value
     */
    public void set(String propertyName, Object value) {
        update(new PropertyUpdater(propertyName), value);
    }

    /**
     * Update both the local instance and the remote master object using
     * specified dynamic object.
     *
     * @param value  dynamic object to update this object with
     */
    public void update(DynamicObject value) {
        update(new DynamicObjectUpdater(), value);
    }

    /**
     * Update both the local instance and the remote master object.
     *
     * @param updater  updater to use
     * @param value    value to update this object with
     */
    @Override
    public void update(Updater updater, Object value) {
        getRepository().update(m_id, updater, value);
        updater.update(this, value);
    }

    /**
     * Invoke a method on a remote master object.
     * <p/>
     * Note that unlike set and update methods, which perform the update on both
     * the local and the remote instance, this method will only execute a remote
     * method.
     * <p/>
     * This allows you to invoke methods that might have side effects in one
     * place only, at the expense of having to manually synchronize local and
     * remote object instances (if such synchronization is necessary at all --
     * in many cases you can simply discard a local instance and refresh it from
     * the remote cache).
     *
     * @param method  the name of the method to invoke
     * @param args    method arguments
     *
     * @return return value of the remote method
     */
    public Object invoke(String method, Object... args) {
        return getRepository().invoke(m_id, method, args);
    }
}
