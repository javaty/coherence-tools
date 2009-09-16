package com.seovic.coherence.loader;


import com.seovic.util.Extractor;
import com.seovic.util.Updater;


/**
 * @author ic  2009.06.09
 */
public class Loader
    {
    private Source source;

    private Target target;

    public Loader(Source source, Target target)
        {
        this.source = source;
        this.target = target;
        }

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
    }

