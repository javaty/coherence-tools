package com.seovic.coherence.util.collections;


import com.seovic.core.Extractor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


/**
 * @author Aleksandar Seovic  2010.11.20
 */
@SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
public class ExtractingCollection<E> implements Collection<E> {
    private Collection   source;
    private Extractor<E> extractor;

    private transient Collection<E> convertedSource;

    public ExtractingCollection(Collection source, Extractor<E> extractor) {
        this.source    = source;
        this.extractor = extractor;
    }

    public int size() {return source.size();}

    public boolean isEmpty() {return source.isEmpty();}

    public boolean contains(Object o) {
        convertSource();
        return convertedSource.contains(o);
    }

    public Iterator<E> iterator() {
        convertSource();
        return convertedSource.iterator();
    }

    public Object[] toArray() {
        return toArray(new Object[source.size()]);
    }

    public <T> T[] toArray(T[] a) {
        convertSource();
        return convertedSource.toArray(a);
    }

    public boolean add(E o) {
        throw new UnsupportedOperationException("This collection is read-only.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("This collection is read-only.");
    }

    public boolean containsAll(Collection<?> c) {
        convertSource();
        return source.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("This collection is read-only.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This collection is read-only.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This collection is read-only.");
    }

    public void clear() {
        throw new UnsupportedOperationException("This collection is read-only.");
    }

    private void convertSource() {
        if (convertedSource != null) return;

        List<E> converted = new ArrayList<E>(source.size());
        for (Object o : source) {
            converted.add(extractor.extract(o));
        }

        convertedSource = converted;
    }
}
