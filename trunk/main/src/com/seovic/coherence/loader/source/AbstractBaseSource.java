package com.seovic.coherence.loader.source;


import com.seovic.coherence.loader.Source;
import com.seovic.core.Extractor;

import java.util.HashMap;
import java.util.Map;


/**
 * @author ic  2009.06.15
 */
public abstract class AbstractBaseSource
        implements Source
    {
    private Map<String, Extractor> extractors;

    protected AbstractBaseSource()
        {
        extractors = new HashMap<String, Extractor>();
        }

    protected abstract Extractor createDefaultExtractor(String propertyName);

    public Extractor getExtractor(String propertyName)
        {
        Extractor extractor = extractors.get(propertyName);
        return extractor != null
               ? extractor
               : createDefaultExtractor(propertyName);
        }

    public void setExtractor(String propertyName, Extractor extractor)
        {
        extractors.put(propertyName, extractor);
        }

    public void beginExport()
        {
        }

    public void endExport()
        {
        }
    }
