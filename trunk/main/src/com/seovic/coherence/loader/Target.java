package com.seovic.coherence.loader;


import com.seovic.lang.Updater;


/**
 * @author ic  2009.06.09
 */
public interface Target
    {
    void beginImport();

    void importItem(Object item);

    void endImport();

    String[] getPropertyNames();

    Object createTargetInstance(Source source, Object sourceItem);

    Updater getUpdater(String propertyName);

    void setUpdater(String propertyName, Updater updater);
    }
