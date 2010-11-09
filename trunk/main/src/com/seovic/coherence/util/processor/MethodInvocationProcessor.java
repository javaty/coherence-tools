package com.seovic.coherence.util.processor;


import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import com.tangosol.util.InvocableMap;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.processor.AbstractProcessor;

import java.io.Serializable;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2010.11.06
 */
public class MethodInvocationProcessor
        extends AbstractProcessor
        implements Serializable, PortableObject {

    private String   name;
    private boolean  mutator;
    private Object[] args;

    public MethodInvocationProcessor() {
    }

    public MethodInvocationProcessor(String name, boolean mutator, Object... args) {
        this.name    = name;
        this.mutator = mutator;
        this.args    = args;
    }

    @Override
    public Object process(InvocableMap.Entry entry) {
        ReflectionExtractor extractor = new ReflectionExtractor(name, args);
        if (mutator) {
            Object value = entry.getValue();
            Object result = extractor.extract(value);
            entry.setValue(value);
            return result;
        }
        else {
            return entry.extract(extractor);
        }
    }

    @Override
    public void readExternal(PofReader reader) throws IOException {
        name    = reader.readString(0);
        mutator = reader.readBoolean(1);
        args    = reader.readObjectArray(2, new Object[0]);
    }

    @Override
    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeString     (0, name);
        writer.writeBoolean    (1, mutator);
        writer.writeObjectArray(2, args);
    }
}
