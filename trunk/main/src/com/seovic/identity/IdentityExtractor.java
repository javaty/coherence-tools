package com.seovic.identity;


/**
 * A strategy interface for identity extractors.
 *
 * @author Aleksandar Seovic  2009.06.09
 */
public interface IdentityExtractor<TId, TEntity>
    {
    /**
     * Extracts identity from a specified source object.
     *
     * @param source  source object to extract identity from
     *
     * @return extracted identity
     */
    public TId extractIdentity(TEntity source);
    }
