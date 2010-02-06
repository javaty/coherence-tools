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


import com.seovic.core.Defaults;
import com.seovic.core.Extractor;
import com.seovic.coherence.util.extractor.KeyExtractorAdapter;

import com.tangosol.util.extractor.AbstractExtractor;
import com.tangosol.util.extractor.IdentityExtractor;

import com.tangosol.util.InvocableMapHelper;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;
import java.io.Serializable;

import java.util.Map;


/**
 * A ValueExtractor implementation that can be used to extract a {@link
 * LookupValue} instance from a target object.
 *
 * @author Aleksandar Seovic  2008.12.19
 */
public class LookupValueExtractor<TId, TDesc>
        extends    AbstractExtractor
        implements Extractor, PortableObject, Serializable
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Deserialization constructor. For internal use only.
     */
    public LookupValueExtractor()
        {
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * an extractor created by {@link Defaults#createExtractor(String)} to
     * extract the description, and a key-targeted {@link IdentityExtractor}
     * to extract the identifier from a cache entry.
     *
     * @param descriptionExpression  an expression that returns a description
     */
    public LookupValueExtractor(String descriptionExpression)
        {
        this(Defaults.createExtractor(descriptionExpression));
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * specified extractor to extract the description, and a key-targeted
     * {@link IdentityExtractor} to extract the identifier from a cache entry.
     *
     * @param descriptionExtractor  description extractor
     */
    public LookupValueExtractor(Extractor descriptionExtractor)
        {
        this(new KeyExtractorAdapter(new IdentityExtractor()),
             descriptionExtractor);
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * default extractors (see {@link Defaults#createExtractor(String)}) to
     * extract both the identifier and the description from a cache entry.
     *
     * @param idExpression           an expression that returns an identifier
     * @param descriptionExpression  an expression that returns a description
     */
    public LookupValueExtractor(String idExpression, String descriptionExpression)
        {
        this(Defaults.createExtractor(idExpression),
             Defaults.createExtractor(descriptionExpression));
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * specified extractors to extract the identifier and the description from
     * a cache entry.
     *
     * @param idExtractor           the identifier extractor
     * @param descriptionExtractor  the description extractor
     */
    public LookupValueExtractor(Extractor idExtractor,
                                Extractor descriptionExtractor)
        {
        m_idExtractor          = idExtractor;
        m_descriptionExtractor = descriptionExtractor;
        }


    // ---- getters and setters ---------------------------------------------

    /**
     * Return identifier extractor.
     *
     * @return identifier extractor
     */
    public Extractor getIdExtractor()
        {
        return m_idExtractor;
        }

    /**
     * Return description extractor.
     *
     * @return description extractor
     */
    public Extractor getDescriptionExtractor()
        {
        return m_descriptionExtractor;
        }

    // ---- AbstractExtractor implementation --------------------------------

    /**
     * Extracts id and description from a map entry.
     *
     * @param entry  mao entry to extract from
     *
     * @return extracted value
     */
    @SuppressWarnings("unchecked")
    public Object extractFromEntry(Map.Entry entry)
        {
        TId   id          = (TId)   InvocableMapHelper.extractFromEntry(m_idExtractor, entry);
        TDesc description = (TDesc) InvocableMapHelper.extractFromEntry(m_descriptionExtractor, entry);

        return new LookupValue<TId, TDesc>(id, description);
        }


    // ---- PortableObject implementation -----------------------------------

    /**
     * Deserialize this object from a POF stream.
     *
     * @param reader  POF reader to use
     *
     * @throws IOException  if an error occurs during deserialization
     */
    public void readExternal(PofReader reader)
            throws IOException
        {
        m_idExtractor          = (Extractor) reader.readObject(0);
        m_descriptionExtractor = (Extractor) reader.readObject(1);
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
        writer.writeObject(0, m_idExtractor);
        writer.writeObject(1, m_descriptionExtractor);
        }


    // ---- Object methods implementation -----------------------------------

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

        LookupValueExtractor that = (LookupValueExtractor) o;

        return m_descriptionExtractor.equals(that.m_descriptionExtractor)
               && m_idExtractor.equals(that.m_idExtractor);

        }

    /**
     * Return hash code for this object.
     *
     * @return this object's hash code
     */
    @Override
    public int hashCode()
        {
        int result = m_idExtractor.hashCode();
        result = 31 * result + m_descriptionExtractor.hashCode();
        return result;
        }

    /**
     * Return string representation of this object.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
        {
        return "LookupValueExtractor{" +
               "idExtractor=" + m_idExtractor +
               ", descriptionExtractor=" + m_descriptionExtractor +
               '}';
        }


    // ---- data members ----------------------------------------------------

    /**
     * Extractor used to extract lookup value identifier from the target object.
     */
    private Extractor m_idExtractor;

    /**
     * Extractor used to extract lookup value description from the target object.
     */
    private Extractor m_descriptionExtractor;
    }

