package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.HashMap;
import java.util.Map;


/**
 * {@link Factory} implementation that creates a <tt>java.util.HashMap</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class HashMapFactory<K,V> extends AbstractFactory<Map<K,V>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<K,V> create() {
        return new HashMap<K,V>();
    }
}