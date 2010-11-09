package com.seovic.core.factory;


import com.seovic.core.Factory;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;


/**
 * Base class for custom factories.
 * <p/>
 * This class implements equals, hashCode, toString and POF serialization for
 * factory classes that have no internal state.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public abstract class AbstractFactory<T> implements Factory<T>, PortableObject {
    @Override
    public boolean equals(Object o) {
        return o != null && getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().toString();
    }

    @Override
    public void readExternal(PofReader reader)throws IOException {}

    @Override
    public void writeExternal(PofWriter writer) throws IOException {}
}
