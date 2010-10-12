package com.seovic.coherence.util.processor;


import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.seovic.core.DynamicObject;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2010.10.11
 */
public class DynamicUpdaterProcessor
        extends AbstractProcessor implements PortableObject {
    private DynamicObject m_updates;
    private boolean m_returnUpdatedValue;

    public DynamicUpdaterProcessor() {
    }

    public DynamicUpdaterProcessor(DynamicObject updates) {
        this(updates, false);
    }

    public DynamicUpdaterProcessor(DynamicObject updates, boolean returnUpdatedValue) {
        m_updates = updates;
        m_returnUpdatedValue = returnUpdatedValue;
    }

    @Override
    public Object process(InvocableMap.Entry entry) {
        Object value = entry.getValue();
        if (value == null) {
            return m_returnUpdatedValue ? null : false;
        }

        m_updates.update(value);
        entry.setValue(value);

        return m_returnUpdatedValue ? value : true;
    }

    @Override
    public void readExternal(PofReader reader)
            throws IOException {
        m_updates = (DynamicObject) reader.readObject(0);
        m_returnUpdatedValue = reader.readBoolean(1);
    }

    @Override
    public void writeExternal(PofWriter writer)
            throws IOException {
        writer.writeObject(0, m_updates);
        writer.writeBoolean(1, m_returnUpdatedValue);
    }
}
