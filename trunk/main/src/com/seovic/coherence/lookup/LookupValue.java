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
     * Deserialize this object from a POF stream.
     *
     * @param reader  POF reader to use
     *
     * @throws IOException  if an error occurs during deserialization
     */
    @SuppressWarnings("unchecked")
    public void readExternal(PofReader reader)
            throws IOException
        {
        id          = (TId)   reader.readObject(0);
        description = (TDesc) reader.readObject(1);
        }

    /**
     * Serialize this object into a POF stream.
     *
     * @param writer  POF writer to use
     *
     * @throws IOException  if an error occurs during serialization
     */
    public void writeExternal(PofWriter writer)
            throws IOException
        {
        writer.writeObject(0, id);
        writer.writeObject(1, description);
        }


    // ---- Object methods --------------------------------------------------

    /**
     * Test objects for equality.
     *
     * @param o  object to compare this object with
     *
     * @return <tt>true</tt> if the specified object is equal to this object
     *         <tt>false</tt> otherwise
     */
    @Override
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
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        return id != null ? id.hashCode() : 0;
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "LookupValue{" +
               "id=" + id +
               ", description=" + description + '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * Lookup value identifier.
     */
    private TId id;

    /**
     * Lookup value description.
     */
    private TDesc description;
    }
