package com.seovic.coherence.util.processor;


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.InvocableMap;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2009.06.24
 */
public class RemoveProcessor
        extends    AbstractProcessor
        implements PortableObject {

    private boolean m_fReturnOldValue;

    public RemoveProcessor() {
    }

    public RemoveProcessor(boolean fReturnOldValue) {
        m_fReturnOldValue = fReturnOldValue;
    }

    public Object process(InvocableMap.Entry entry) {
        Object oldValue = m_fReturnOldValue ? entry.getValue() : null;
        entry.remove(false);
        return oldValue;
    }
    public void readExternal(PofReader reader) throws IOException {
        m_fReturnOldValue = reader.readBoolean(0);
    }

    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeBoolean(0, m_fReturnOldValue);
    }
}
