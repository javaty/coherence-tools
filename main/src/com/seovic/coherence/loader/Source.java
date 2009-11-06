package com.seovic.coherence.loader;


import com.seovic.core.Extractor;
import java.io.Serializable;


/**
 * An iterable source that the items can be loaded from.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public interface Source
        extends Iterable, Serializable
    {
    /**
     * Called by the loader to inform source that the loading process is
     * about to start.
     * <p/>
     * This is a lifecycle method that allows implementations to perform any
     * preliminary one-time set up before the load starts.
     */
    void beginExport();

    /**
     * Called by the loader to inform source that the loading process is
     * finished.
     * <p/>
     * This is a lifecycle method that allows implementations to perform any
     * necessary cleanup after the load is finished.
     */
    void endExport();

    /**
     * Return extractor for the specified property.
     *
     * @param propertyName  property name
     *
     * @return extractor that should be used for the specified property
     */
    Extractor getExtractor(String propertyName);

    /**
     * Set extractor for the specified property.
     *
     * @param propertyName  property name
     * @param extractor     extractor that should be used for the specified
     *                      property
     */
    void setExtractor(String propertyName, Extractor extractor);
    }
