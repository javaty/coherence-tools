package com.seovic.loader;


import com.seovic.core.Extractor;

import java.io.Serializable;
import java.util.Set;


/**
 * An iterable source that the items can be loaded from.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public interface Source
        extends Iterable, Serializable {
    /**
     * Called by the loader to inform source that the loading process is about
     * to start.
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
     * @param targetField  target field
     *
     * @return extractor that should be used for the specified target field
     */
    Extractor getExtractor(String targetField);

    /**
     * Set extractor for the specified target field.
     *
     * @param targetField  target field
     * @param extractor    extractor that should be used for the specified
     *                     target field
     */
    void setExtractor(String targetField, Extractor extractor);

    /**
     * Maps field from this source to a target field using default
     * extractor.
     *
     * @param targetField  target field to set
     * @param sourceField  source field to read
     *
     * @return this source
     */
    Source map(String targetField, String sourceField);

    /**
     * Maps field from this source to a target field using specified
     * extractor.
     *
     * @param targetField      target field to set
     * @param sourceExtractor  source extractor to use
     *
     * @return this source
     */
    Source map(String targetField, Extractor sourceExtractor);

    /**
     * Return explicitly mapped property names.
     * <p/>
     * This method will be called if the {@link Loader} mapping mode is set to
     * {@link MappingMode#EXPLICIT} to determine a list of properties that should
     * be copied from source to target.
     *
     * @return the names of the explicitly mapped target properties
     */
    Set<String> getPropertyNames();
}
