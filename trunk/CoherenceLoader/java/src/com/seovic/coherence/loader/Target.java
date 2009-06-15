package com.seovic.coherence.loader;

/**
 * @author ic  2009.06.09
 */
public interface Target {
    void beginImport();
    void importItem(Object item);
    void endImport();

    String[] getPropertyNames();
    Object createTargetInstance();

    PropertySetter getPropertySetter(String property);
    void setPropertySetter(String property, PropertySetter setter);
}
