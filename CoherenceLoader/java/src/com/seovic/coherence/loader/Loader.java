package com.seovic.coherence.loader;


/**
 * @author ic  2009.06.09
 */
public class Loader {
    private Source source;
    private Target target;

    public Loader(Source source, Target target) {
        this.source = source;
        this.target = target;
    }

    public void load() {
        target.beginImport();
        String[] propertyNames = target.getPropertyNames();

        for (Object sourceItem : source) {
            Object targetItem = target.createTargetInstance();
            for (String property : propertyNames) {
                PropertyGetter getter = source.getPropertyGetter(property);
                PropertySetter setter = target.getPropertySetter(property);
                setter.setValue(targetItem, getter.getValue(sourceItem));
            }
            target.importItem(targetItem);
        }
        target.endImport();
    }
}

