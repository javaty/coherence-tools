package com.seovic.coherence.index;


import com.seovic.core.Defaults;
import com.seovic.core.Extractor;

import java.util.Comparator;


/**
 * @author Aleksandar Seovic  2010.09.09
 */
public class IndexDefinition {
    private Extractor  m_extractor;
    private boolean    m_sorted;
    private Comparator m_comparator;

    public IndexDefinition(String expression, boolean sorted, Comparator comparator) {
        this(Defaults.createExtractor(expression), sorted, comparator);
    }

    public IndexDefinition(Extractor extractor, boolean sorted, Comparator comparator) {
        m_extractor  = extractor;
        m_sorted     = sorted;
        m_comparator = comparator;
    }

    public Extractor getExtractor() {
        return m_extractor;
    }

    public boolean isSorted() {
        return m_sorted;
    }

    public Comparator getComparator() {
        return m_comparator;
    }

    @Override
    public String toString() {
        return "IndexDefinition{" +
               "extractor=" + m_extractor +
               ", sorted=" + m_sorted +
               ", comparator=" + m_comparator +
               '}';
    }
}
