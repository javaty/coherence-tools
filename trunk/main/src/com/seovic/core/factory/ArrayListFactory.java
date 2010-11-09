package com.seovic.core.factory;


import com.seovic.core.Factory;

import java.util.ArrayList;
import java.util.List;


/**
 * {@link Factory} implementation that creates a <tt>java.util.ArrayList</tt>
 * instance.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class ArrayListFactory<E> extends AbstractFactory<List<E>> {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> create() {
        return new ArrayList<E>();
    }
}
