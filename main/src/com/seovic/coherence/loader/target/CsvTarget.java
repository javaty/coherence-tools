package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.Source;
import com.seovic.coherence.loader.Target;

import com.seovic.core.Updater;
import com.seovic.core.updater.MapUpdater;

import java.io.IOException;
import java.io.Writer;

import java.util.HashMap;
import java.util.Map;

import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;


/**
 * A {@link Target} implementation that writes items into a CSV file.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public class CsvTarget
        extends AbstractBaseTarget
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct CsvTarget instance.
     *
     * @param writer         writer to use
     * @param propertyNames  attribute names
     */
    public CsvTarget(Writer writer, String propertyNames)
        {
        this(writer, propertyNames.split(","));
        }

    /**
     * Construct CsvTarget instance.
     *
     * @param writer         writer to use
     * @param propertyNames  attribute names
     */
    public CsvTarget(Writer writer, String... propertyNames)
        {
        this.m_writer = new CsvMapWriter(writer,
                                       CsvPreference.STANDARD_PREFERENCE);
        this.m_propertyNames = propertyNames;
        }


    // ---- AbstractBaseTarget implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Updater createDefaultUpdater(String propertyName)
        {
        return new MapUpdater(propertyName);
        }


    // ---- Source implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void beginImport()
        {
        try
            {
            m_writer.writeHeader(m_propertyNames);
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked"})
    public void importItem(Object item)
        {
        try
            {
            m_writer.write((Map<String, ?>) item, m_propertyNames);
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }

        }

    /**
     * {@inheritDoc}
     */
    public void endImport()
        {
        try
            {
            m_writer.close();
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames()
        {
        return m_propertyNames;
        }

    /**
     * {@inheritDoc}
     */
    public Object createTargetInstance(Source source, Object sourceItem)
        {
        return new HashMap<String, Object>();
        }

    
    // ---- data members ----------------------------------------------------

    /**
     * A writer to use.
     */
    private ICsvMapWriter m_writer;

    /**
     * Property names.
     */
    private String[] m_propertyNames;
    }
