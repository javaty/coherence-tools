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

package com.seovic.coherence.lookup;


import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;
import java.io.Serializable;


/**
 * An object representing a single lookup value.
 *
 * @author Aleksandar Seovic  2008.12.19
 */
public class LookupValue<TId, TDesc>
        implements PortableObject, Serializable
    {
    // ---- data members ----------------------------------------------------

    /**
     * Lookup value identifier.
     */
    private TId id;

    /**
     * Lookup value description.
     */
    private TDesc description;


    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor. For internal use only.
     */
    public LookupValue()
        {
        }

    /**
     * Construct <tt>LookupValue</tt> instance.
     *
     * @param id           lookup value identifier
     * @param description  lookup value description
     */
    public LookupValue(TId id, TDesc description)
        {
        this.id          = id;
        this.description = description;
        }


    // ---- accessors -------------------------------------------------------

    /**
     * Return lookup value identifier.
     *
     * @return lookup value identifier
     */
    public TId getId()
        {
        return id;
        }

    /**
     * Return lookup value description.
     *
     * @return lookup value description
     */
    public TDesc getDescription()
        {
        return description;
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void readExternal(PofReader pofReader)
            throws IOException
        {
        id          = (TId)   pofReader.readObject(0);
        description = (TDesc) pofReader.readObject(1);
        }

    /**
     * {@inheritDoc}
     */
    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeObject(0, id);
        pofWriter.writeObject(1, description);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o)
        {
        if (this == o)
            {
            return true;
            }
        if (o == null || getClass() != o.getClass())
            {
            return false;
            }

        LookupValue that = (LookupValue) o;

        return !(description != null
                 ? !description.equals(that.description)
                 : that.description != null)
               && !(id != null
                 ? !id.equals(that.id)
                 : that.id != null);
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
        {
        return id != null ? id.hashCode() : 0;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
        {
        return "LookupValue(" +
               "Id = " + id +
               ", Description = " + description + ")";
        }
    }
