package com.seovic.coherence.util.extractor;


import com.seovic.core.Extractor;
import com.seovic.core.PropertyList;
import com.seovic.core.DynamicObject;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2010.10.11
 */
public class DynamicObjectExtractor
        implements Extractor<DynamicObject>, PortableObject {

    private PropertyList m_properties;

    public DynamicObjectExtractor() {
    }

    public DynamicObjectExtractor(PropertyList properties) {
        m_properties = properties;
    }

    @Override
    public DynamicObject extract(Object target) {
        return m_properties == null
               ? new DynamicObject(target)
               : new DynamicObject(target, m_properties);
    }

    @Override
    public void readExternal(PofReader reader) throws IOException {
        m_properties = (PropertyList) reader.readObject(0);
    }

    @Override
    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeObject(0, m_properties);
    }
}
