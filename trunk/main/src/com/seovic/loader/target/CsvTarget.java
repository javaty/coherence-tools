package com.seovic.loader.target;


import com.seovic.loader.Source;
import com.seovic.loader.Target;

import com.seovic.core.Updater;
import com.seovic.core.updater.MapUpdater;
import com.seovic.io.WriterFactory;

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
     * @param writerFactory  writer factory that should be used to create Writer
     * @param propertyNames  comma-separated property names
     */
    public CsvTarget(WriterFactory writerFactory, String propertyNames)
        {
        this(writerFactory, propertyNames.split(","));
        }

    /**
     * Construct CsvTarget instance.
     *
     * @param writerFactory  writer factory that should be used to create Writer
     * @param propertyNames  property names
     */
    public CsvTarget(WriterFactory writerFactory, String... propertyNames)
        {
        m_writerFactory = writerFactory;
        m_propertyNames = propertyNames;
        }

    /**
     * Construct CsvTarget instance.
     * <p/>
     * This constructor should only be used when using CsvTarget in process.
     * In situations where this object might be serialized and used in a
     * remote process (as part of remote batch load job, for example), you
     * should use the constructor that accepts {@link WriterFactory} as an
     * argument instead.
     *
     * @param writer         writer to use
     * @param propertyNames  comma-separated property names
     */
    public CsvTarget(Writer writer, String propertyNames)
        {
        this(writer, propertyNames.split(","));
        }

    /**
     * Construct CsvTarget instance.
     * <p/>
     * This constructor should only be used when using CsvTarget in process.
     * In situations where this object might be serialized and used in a
     * remote process (as part of remote batch load job, for example), you
     * should use the constructor that accepts {@link WriterFactory} as an
     * argument instead.
     *
     * @param writer         writer to use
     * @param propertyNames  property names
     */
    public CsvTarget(Writer writer, String... propertyNames)
        {
        m_writer = writer;
        m_propertyNames = propertyNames;
        }


    // ---- AbstractBaseTarget implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Updater createDefaultUpdater(String propertyName)
        {
        return new MapUpdater(propertyName);
        }


    // ---- Target implementation -------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void beginImport()
        {
        try
            {
            if (m_writer == null)
                {
                m_writer = m_writerFactory.createWriter();
                }
            CsvPreference preferences =
                    new CsvPreference(m_quoteChar, m_delimiterChar, m_endOfLineSymbols);
            m_csvWriter = new CsvMapWriter(m_writer, preferences);
            m_csvWriter.writeHeader(m_propertyNames);
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
            m_csvWriter.write((Map<String, ?>) item, m_propertyNames);
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
            m_csvWriter.close();
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

    
    // ---- public API ------------------------------------------------------

    /**
     * Set the delimiter character for CSV fields (default is comma).
     *
     * @param delimiterChar  delimiter character
     */
    public void setDelimiterChar(char delimiterChar)
        {
        m_delimiterChar = delimiterChar;
        }

    /**
     * Set the quote character (default is double quote).
     *
     * @param quoteChar  quote character
     */
    public void setQuoteChar(char quoteChar)
        {
        m_quoteChar = quoteChar;
        }

    /**
     * Set the end-of-line characters.
     *
     * @param endOfLineSymbols  end-of-line characters
     */
    public void setEndOfLineSymbols(String endOfLineSymbols)
        {
        m_endOfLineSymbols = endOfLineSymbols;
        }


    // ---- data members ----------------------------------------------------

    /**
     * A factory that should be used to create writer.
     */
    private WriterFactory m_writerFactory;

    /**
     * A writer to use.
     */
    private transient Writer m_writer;

    /**
     * A CSV writer to use.
     */
    private transient ICsvMapWriter m_csvWriter;

    /**
     * Property names.
     */
    private String[] m_propertyNames;

    /**
     * The delimiter character for CSV fields.
     */
    private char m_delimiterChar =
            (char) CsvPreference.STANDARD_PREFERENCE.getDelimiterChar();

    /**
     * The quote character.
     */
    private char m_quoteChar =
            (char) CsvPreference.STANDARD_PREFERENCE.getQuoteChar();

    /**
     * The end-of-line characters.
     */
    private String m_endOfLineSymbols =
            CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols();
    }
