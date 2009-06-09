package com.seovic.coherence.loader;

/**
 * @author ic  2009.06.09
 */
public interface Target {
    void registerPropertyMapper(String targetProperty, PropertyMapper mapper);

    void beginImport();
    void importSingle(Object item);
    void endImport();
}
