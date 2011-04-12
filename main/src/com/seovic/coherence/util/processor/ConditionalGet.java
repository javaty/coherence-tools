package com.seovic.coherence.util.processor;


import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.Filter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;


/**
 * @author Aleksandar Seovic  2011.04.11
 */
public class ConditionalGet extends AbstractProcessor implements PortableObject {
    private Filter condition;

    public ConditionalGet() {
    }

    public ConditionalGet(Filter condition) {
        this.condition = condition;
    }

    @Override
    public Object process(InvocableMap.Entry entry) {
        Object value = entry.getValue();
        return condition.evaluate(value) ? value : null;
    }


    @Override
    public void readExternal(PofReader reader) throws IOException {
        condition = (Filter) reader.readObject(0);
    }

    @Override
    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeObject(0, condition);
    }
}
