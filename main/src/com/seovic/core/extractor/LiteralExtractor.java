package com.seovic.core.extractor;


import com.seovic.core.Extractor;


/**
 * Extractor implementation that always returns the literal value it was
 * constructed with.
 * 
 * @author Aleksandar Seovic  2012.04.05
 */
public class LiteralExtractor
        implements Extractor {
    private Object m_literal;

    /**
     * Construct LiteralExtractor instance.
     *
     * @param literal  the literal value to return from {@link #extract(Object)} method
     */
    public LiteralExtractor(Object literal) {
        m_literal = literal;
    }

    /**
     * Return the literal value this instance was constructed with.
     *
     * @param o  normally an object to extract value from, but it is ignored by
     *           this particular implementation
     *
     * @return the literal value this instance was constructed with
     */
    @Override
    public Object extract(Object o) {
        return m_literal;
    }
}
