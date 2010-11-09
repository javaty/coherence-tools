package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.HashSet;
import java.util.Set;


/**
 * {@link Factory} implementation that creates a <tt>java.util.HashSet</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class HashSetFactory<E> extends AbstractFactory<Set<E>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> create() {
        return new HashSet<E>();
    }
}