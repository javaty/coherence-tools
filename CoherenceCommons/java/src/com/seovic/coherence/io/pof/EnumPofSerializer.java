/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.coherence.io.pof;


import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofContext;

import java.io.IOException;


/**
 * POF serializer that can be used to serialize all enum values.
 *
 * @author Aleksandar Seovic  2008.10.24
 */
@SuppressWarnings("unchecked")
public class EnumPofSerializer
        implements PofSerializer
    {
    // ---- PofSerializer implementation ------------------------------------

    /**
     * {@inheritDoc}
     */
    public void serialize(PofWriter writer, Object o)
            throws IOException
        {
        if (!o.getClass().isEnum())
            {
            throw new IllegalArgumentException(
                    "EnumPofSerializer can only be used to serialize enum types.");
            }

        writer.writeString(0, ((Enum) o).name());
        writer.writeRemainder(null);
        }

    /**
     * {@inheritDoc}
     */
    public Object deserialize(PofReader reader)
            throws IOException
        {

        PofContext pofContext = reader.getPofContext();
        Class      enumType   = pofContext.getClass(reader.getUserTypeId());

        if (!enumType.isEnum())
            {
            throw new IllegalArgumentException(
                    "EnumPofSerializer can only be used to deserialize enum types.");
            }

        Enum enumValue = Enum.valueOf(enumType, reader.readString(0));
        reader.readRemainder();

        return enumValue;
        }
    }

