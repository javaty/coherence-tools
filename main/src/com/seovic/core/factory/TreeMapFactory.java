package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.Map;
import java.util.TreeMap;


/**
 * {@link Factory} implementation that creates a <tt>java.util.TreeMap</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class TreeMapFactory<K,V> extends AbstractFactory<Map<K,V>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<K,V> create() {
        return new TreeMap<K,V>();
    }
}