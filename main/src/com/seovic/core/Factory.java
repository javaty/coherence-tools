package com.seovic.core;


/**
 * An interface that should be implemented by object factories.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public interface Factory<T> {
    /**
     * Create object instance.
     * @return created object instance
     */
    T  create();
}
