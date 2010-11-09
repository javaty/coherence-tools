package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.LinkedList;
import java.util.List;


/**
 * {@link Factory} implementation that creates a <tt>java.util.LinkedList</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class LinkedListFactory<E> extends AbstractFactory<List<E>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> create() {
        return new LinkedList<E>();
    }
}