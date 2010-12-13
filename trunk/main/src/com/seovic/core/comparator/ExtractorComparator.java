package com.seovic.core.comparator;


import java.util.Comparator;
import java.io.Serializable;
import java.io.IOException;
import com.seovic.core.Extractor;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;


/**
 * Compares objects based on the values of the extracted attributes.
 *
 * The extracted attributes must be of a primitive type or implement
 * <tt>
 *
 * @author Aleksandar Seovic  2010.09.09
 */
public class ExtractorComparator implements Comparator, Serializable, PortableObject {
    private Extractor m_extractor;

    public ExtractorComparator() {
    }

    public ExtractorComparator(Extractor extractor) {
        m_extractor = extractor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        Comparable a1 = (Comparable) m_extractor.extract(o1);
        Comparable a2 = (Comparable) m_extractor.extract(o2);

        if (a1 == null)
            {
            return a2 == null ? 0 : -1;
            }

        if (a2 == null)
            {
            return +1;
            }

        return a1.compareTo(a2);
    }

    @Override
    public void readExternal(PofReader in) throws IOException {
        m_extractor = (Extractor) in.readObject(0);
    }

    @Override
    public void writeExternal(PofWriter out) throws IOException {
        out.writeObject(0, m_extractor);
    }
}
