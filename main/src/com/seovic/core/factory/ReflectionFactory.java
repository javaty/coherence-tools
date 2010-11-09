package com.seovic.core.factory;


import com.seovic.core.Factory;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;


/**
 * {@link Factory} implementation that uses reflection to create an instance
 * of a specified class.
 * <p/>
 * Note that the specified class must have public default constructor in order
 * for this factory to work.
 *
 * @author Aleksandar Seovic  2010.11.08
 */
public class ReflectionFactory<T> extends AbstractFactory<T> {
    private String className;

    /**
     * Deserialization constructor (for internal use only).
     */
    public ReflectionFactory() {
    }

    /**
     * Construct a <tt>ReflectionFactory</tt> instance.
     *
     * @param className fully qualified name of the class this factory will create
     */
    public ReflectionFactory(String className) {
        if (className == null) throw new IllegalArgumentException("Class name cannot be null");
        
        this.className = className;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T create() {
        try {
            return (T) Class.forName(className).newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o)
               && className.equals(((ReflectionFactory) o).className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        return "ReflectionFactory{" +
               "className='" + className + '\'' +
               '}';
    }

    /**
     * Deserialize this object from a POF stream.
     *
     * @param reader  POF reader to use
     *
     * @throws IOException  if an error occurs during deserialization
     */
    @Override
    public void readExternal(PofReader reader) throws IOException {
        className = reader.readString(0);
    }

    /**
     * Serialize this object into a POF stream.
     *
     * @param writer  POF writer to use
     *
     * @throws IOException  if an error occurs during serialization
     */
    @Override
    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeString(0, className);
    }
}
