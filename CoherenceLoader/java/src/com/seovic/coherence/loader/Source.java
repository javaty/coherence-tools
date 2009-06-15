package com.seovic.coherence.loader;

/**
 * @author ic  2009.06.09
 */
public interface Source extends Iterable {
    PropertyGetter getPropertyGetter(String propertyName);
    void setPropertyGetter(String propertyName, PropertyGetter propertyGetter);
}
