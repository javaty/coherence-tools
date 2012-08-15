package com.seovic.loader;


import com.seovic.core.Extractor;
import com.seovic.core.Updater;
import java.util.Set;


/**
 * Asbtract base class for {@link Loader} implementations.
 *
 * @author Aleksandar Seovic  2012.04.06
 */
public abstract class AbstractLoader
        implements Loader {

    private MappingMode mode = MappingMode.AUTO;

    // ---- Loader implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void load() {
        Source source = getSource();
        Target target = getTarget();
        source.beginExport();
        target.beginImport();
        Set<String> propertyNames = mode.equals(MappingMode.AUTO)
                                 ? target.getPropertyNames()
                                 : source.getPropertyNames();

        for (Object sourceItem : source) {
            Object targetItem = target.createTargetInstance(source, sourceItem);
            for (String property : propertyNames) {
                Extractor extractor = source.getExtractor(property);
                Updater updater = target.getUpdater(property);
                updater.update(targetItem, extractor.extract(sourceItem));
            }
            target.importItem(targetItem);
        }
        source.endExport();
        target.endImport();
    }

    @Override
    public MappingMode getMappingMode() {
        return mode;
    }

    @Override
    public void setMappingMode(MappingMode mode) {
        this.mode = mode;
    }

    // ---- abstract methods ------------------------------------------------

    /**
     * Return fully configured {@link Source} instance.
     *
     * @return Source instance
     */
    protected abstract Source getSource();

    /**
     * Return fully configured {@link Target} instance.
     *
     * @return Target instance
     */
    protected abstract Target getTarget();
}
