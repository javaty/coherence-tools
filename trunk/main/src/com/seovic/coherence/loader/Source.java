package com.seovic.coherence.loader;


import com.seovic.core.Extractor;


/**
 * @author ic  2009.06.09
 */
public interface Source extends Iterable
    {
    void beginExport();

    void endExport();

    Extractor getExtractor(String propertyName);

    void setExtractor(String propertyName, Extractor extractor);
    }
