package com.seovic.coherence.loader;


import com.seovic.core.Extractor;
import com.seovic.core.Updater;


/**
 * Default loader implementation.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public class DefaultLoader
        implements Loader
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct DefaultLoader instance.
     *
     * @param source  source to load items from
     * @param target  target to load items into
     */
    public DefaultLoader(Source source, Target target)
        {
        this.source = source;
        this.target = target;
        }


    // ---- Loader implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void load()
        {
        source.beginExport();
        target.beginImport();
        String[] propertyNames = target.getPropertyNames();

        for (Object sourceItem : source)
            {
            Object targetItem = target.createTargetInstance(source, sourceItem);
            for (String property : propertyNames)
                {
                Extractor extractor = source.getExtractor(property);
                Updater   updater   = target.getUpdater(property);
                updater.update(targetItem, extractor.extract(sourceItem));
                }
            target.importItem(targetItem);
            }
        source.endExport();
        target.endImport();
        }

    // ---- data members ----------------------------------------------------

    /**
     * Source to load items from.
     */
    private Source source;

    /**
     * Target to load items into.
     */
    private Target target;
    }

