package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * {@link Factory} implementation that creates a <tt>java.util.LinkedHashMap</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class LinkedHashMapFactory<K,V> extends AbstractFactory<Map<K,V>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<K,V> create() {
        return new LinkedHashMap<K,V>();
    }
}