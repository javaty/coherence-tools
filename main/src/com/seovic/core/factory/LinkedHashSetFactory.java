package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.Set;
import java.util.LinkedHashSet;


/**
 * {@link Factory} implementation that creates a <tt>java.util.LinkedHashSet</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class LinkedHashSetFactory<E> extends AbstractFactory<Set<E>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> create() {
        return new LinkedHashSet<E>();
    }
}