package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.TreeSet;
import java.util.Set;


/**
 * {@link Factory} implementation that creates a <tt>java.util.TreeSet</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class TreeSetFactory<E> extends AbstractFactory<Set<E>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> create() {
        return new TreeSet<E>();
    }
}