package com.seovic.core.updater;


import com.seovic.core.Updater;
import com.seovic.core.DynamicObject;

import java.io.Serializable;


/**
 * @author Aleksandar Seovic  2010.10.13
 */
public class DynamicObjectUpdater implements Updater, Serializable {
    @Override
    public void update(Object target, Object value) {
        if (value instanceof DynamicObject) {
            ((DynamicObject) value).update(target);
        }
        else {
            throw new IllegalArgumentException("Value to update target with must me an instance of DynamicObject");
        }
    }
}
