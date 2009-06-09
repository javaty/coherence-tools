package com.seovic.coherence.identity;

/**
 * A strategy interface for identity extractors.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.09
 */
public interface IdentityExtractor<ID, SOURCE> {
    /**
     * Extracts identity from a specified source object.
     *
     * @param source  source object to extract identity from
     *
     * @return extracted identity
     */
    public ID extractIdentity(SOURCE source);
}
