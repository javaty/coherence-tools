package com.seovic.loader.source;


import com.seovic.core.Extractor;
import com.seovic.loader.Source;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;


/**
 * Abstract base class for {@link Source} implementations.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public abstract class AbstractBaseSource
        implements Source {
    // ---- constructors ----------------------------------------------------

    /**
     * Default constructor.
     */
    protected AbstractBaseSource() {
        m_extractors = new HashMap<String, Extractor>();
    }


    // ---- abstract methods ------------------------------------------------

    /**
     * Create default extractor for the specified property.
     *
     * @param propertyName property to create an extractor for
     *
     * @return property extractor instance
     */
    protected abstract Extractor createDefaultExtractor(String propertyName);


    // ---- Source implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Extractor getExtractor(String propertyName) {
        Extractor extractor = m_extractors.get(propertyName);
        if (extractor == null) {
            extractor = createDefaultExtractor(propertyName);
            m_extractors.put(propertyName, extractor);
        }
        return extractor;
    }

    /**
     * {@inheritDoc}
     */
    public void setExtractor(String propertyName, Extractor extractor) {
        m_extractors.put(propertyName, extractor);
    }

    /**
     * {@inheritDoc}
     */
    public Source map(String targetField, String sourceField) {
        setExtractor(targetField, createDefaultExtractor(sourceField));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Source map(String targetField, Extractor sourceExtractor) {
        setExtractor(targetField, sourceExtractor);
        return this;
    }

    @Override
    public Set<String> getPropertyNames() {
        return m_extractors.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public void beginExport() {
    }

    /**
     * {@inheritDoc}
     */
    public void endExport() {
    }


    // ---- helper methods --------------------------------------------------

    /**
     * Return a Resource represented by the specified location.
     *
     * @param location resource location
     *
     * @return a resource
     */
    protected Resource getResource(String location) {
        return new DefaultResourceLoader().getResource(location);
    }

    /**
     * Create a reader for the specified resource.
     *
     * @param resource resource to create reader for
     *
     * @return reader for the specified resource
     */
    protected Reader createResourceReader(Resource resource) {
        try {
            return new InputStreamReader(resource.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // ---- data members ----------------------------------------------------

    /**
     * A map of registered property extractors for this source.
     */
    private Map<String, Extractor> m_extractors;
}
