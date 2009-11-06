package com.seovic.coherence.loader.source;


import com.seovic.coherence.loader.Source;
import com.seovic.core.Extractor;

import java.util.HashMap;
import java.util.Map;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.DefaultResourceLoader;


/**
 * Abstract base class for {@link Source} implementations.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public abstract class AbstractBaseSource
        implements Source
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Default constructor.
     */
    protected AbstractBaseSource()
        {
        m_extractors = new HashMap<String, Extractor>();
        }


    // ---- abstract methods ------------------------------------------------

    /**
     * Create default extractor for the specified property.
     *
     * @param propertyName  property to create an extractor for
     *
     * @return property extractor instance
     */
    protected abstract Extractor createDefaultExtractor(String propertyName);


    // ---- Source implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Extractor getExtractor(String propertyName)
        {
        Extractor extractor = m_extractors.get(propertyName);
        if (extractor == null)
            {
            extractor = createDefaultExtractor(propertyName);
            m_extractors.put(propertyName, extractor);
            }
        return extractor;
        }

    /**
     * {@inheritDoc}
     */
    public void setExtractor(String propertyName, Extractor extractor)
        {
        m_extractors.put(propertyName, extractor);
        }

    /**
     * {@inheritDoc}
     */
    public void beginExport()
        {
        }

    /**
     * {@inheritDoc}
     */
    public void endExport()
        {
        }

    
    // ---- helper methods --------------------------------------------------

    /**
     * Return a Resource represented by the specified location.
     *
     * @param location  resource location
     *
     * @return a resource
     */
    protected Resource getResource(String location)
        {
        return new DefaultResourceLoader().getResource(location);
        }

    /**
     * Create a reader for the specified resource.
     *
     * @param resource  resource to create reader for
     *
     * @return reader for the specified resource
     */
    protected Reader createResourceReader(Resource resource)
        {
        try
            {
            return new InputStreamReader(resource.getInputStream());
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        }


    // ---- data members ----------------------------------------------------

    /**
     * A map of registered property extractors for this source.
     */
    private Map<String, Extractor> m_extractors;
    }
