package com.seovic.coherence.io.pof;


import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofContext;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2010.10.13
 */
public class NullPofSerializer implements PofSerializer {
    @Override
    public void serialize(PofWriter writer, Object o) throws IOException {
        writer.writeRemainder(null);
    }

    @Override
    public Object deserialize(PofReader reader) throws IOException {
        PofContext pofContext = reader.getPofContext();
        Class      type       = pofContext.getClass(reader.getUserTypeId());

        reader.readRemainder();
        
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
