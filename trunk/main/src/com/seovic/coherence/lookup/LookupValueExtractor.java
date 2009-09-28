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


import com.seovic.core.extractor.PropertyExtractor;

import com.tangosol.util.extractor.AbstractExtractor;
import com.tangosol.util.extractor.KeyExtractor;
import com.tangosol.util.extractor.IdentityExtractor;

import com.tangosol.util.ValueExtractor;
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
        implements PortableObject, Serializable
    {
    // ---- data members ----------------------------------------------------

    /**
     * Extractor used to extract lookup value identifier from the target object.
     */
    private ValueExtractor m_idExtractor;

    /**
     * Extractor used to extract lookup value description from the target object.
     */
    private ValueExtractor m_descriptionExtractor;


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
     * {@link PropertyExtractor} to extract the description, and a key-targeted
     * {@link IdentityExtractor} to extract the identifier from a cache entry.
     *
     * @param descriptionProperty  the name of the description property
     */
    public LookupValueExtractor(String descriptionProperty)
        {
        this(new PropertyExtractor(descriptionProperty));
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * specified value extractor to extract the description, and a key-targeted
     * {@link IdentityExtractor} to extract the identifier from a cache entry.
     *
     * @param descriptionExtractor  the description extractor
     */
    public LookupValueExtractor(ValueExtractor descriptionExtractor)
        {
        this(new KeyExtractor(new IdentityExtractor()), descriptionExtractor);
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * {@link PropertyExtractor}s to extract both the identifier and the
     * description from a cache entry.
     *
     * @param idProperty           the name of the identifier property
     * @param descriptionProperty  the name of the description property
     */
    public LookupValueExtractor(String idProperty, String descriptionProperty)
        {
        this(new PropertyExtractor(idProperty),
             new PropertyExtractor(descriptionProperty));
        }

    /**
     * Construct <tt>LookupValueExtractor</tt> instance.
     * <p/>
     * This constructor will create a <tt>LookupValueExtractor</tt> that uses
     * specified value extractors to extract the identifier and the description
     * from a cache entry.
     *
     * @param idExtractor           the identifier extractor
     * @param descriptionExtractor  the description extractor
     */
    public LookupValueExtractor(ValueExtractor idExtractor,
                                ValueExtractor descriptionExtractor)
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
    public ValueExtractor getIdExtractor()
        {
        return m_idExtractor;
        }

    /**
     * Return description extractor.
     *
     * @return description extractor
     */
    public ValueExtractor getDescriptionExtractor()
        {
        return m_descriptionExtractor;
        }

    // ---- AbstractExtractor implementation --------------------------------

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void readExternal(PofReader pofReader)
            throws IOException
        {
        m_idExtractor          = (ValueExtractor) pofReader.readObject(0);
        m_descriptionExtractor = (ValueExtractor) pofReader.readObject(1);
        }

    /**
     * {@inheritDoc}
     */
    public void writeExternal(PofWriter pofWriter)
            throws IOException
        {
        pofWriter.writeObject(0, m_idExtractor);
        pofWriter.writeObject(1, m_descriptionExtractor);
        }


    // ---- Object methods implementation -----------------------------------

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
        {
        int result = m_idExtractor.hashCode();
        result = 31 * result + m_descriptionExtractor.hashCode();
        return result;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
        {
        return "LookupValueExtractor(" +
               "idExtractor = " + m_idExtractor +
               ", descriptionExtractor = " + m_descriptionExtractor +
               ")";
        }
    }

