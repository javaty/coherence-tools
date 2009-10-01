package com.seovic.coherence.loader.source;


import com.seovic.core.Extractor;
import com.seovic.core.extractor.MapExtractor;
import com.seovic.coherence.loader.Source;

import java.io.IOException;
import java.io.Reader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;


/**
 * A {@link Source} implementation that reads items to load from a CSV file.
 *
 * @author Aleksandar Seovic/Ivan Cikic  2009.06.15
 */
public class CsvSource
        extends AbstractBaseSource
    {
    // ---- constructors ----------------------------------------------------

    /**
     * Construct a CsvSource instance.
     *
     * @param reader  reader to use to read CSV file with
     */
    public CsvSource(Reader reader)
        {
        this.m_reader = reader;
        }


    // ---- Iterable implementation -----------------------------------------

    /**
     * Return an iterator over this source.
     *
     * @return a source iterator
     */
    public Iterator iterator()
        {
        return new CsvIterator(new CsvListReader(m_reader, m_preferences));
        }


    // ---- public API ------------------------------------------------------

    /**
     * Set the delimiter character for CSV fields (default is comma).
     *
     * @param delimiter  delimiter character
     */
    public void setDelimiterChar(char delimiter)
        {
        m_preferences.setDelimiterChar(delimiter);
        }

    /**
     * Set the quote character (default is double quote).
     *
     * @param quote  quote character
     */
    public void setQuoteChar(char quote)
        {
        m_preferences.setQuoteChar(quote);
        }

    /**
     * Set end-of-line characters.
     *
     * @param endOfLineSymbols  end-of-line characters
     */
    public void setEndOfLineSymbols(String endOfLineSymbols)
        {
        m_preferences.setEndOfLineSymbols(endOfLineSymbols);
        }


    // ---- AbstractBaseSource implementation -------------------------------

    /**
     * {@inheritDoc}
     */
    protected Extractor createDefaultExtractor(String propertyName)
        {
        return new MapExtractor(propertyName);
        }


    // ---- inner class: CsvIterator ----------------------------------------

    /**
     * Iterator implementation for CsvSource.
     */
    public class CsvIterator
            implements Iterator
        {
        // ---- constructors --------------------------------------------

        /**
         * Construct CsvIterator instance.
         *
         * @param reader  reader to use
         */
        public CsvIterator(ICsvListReader reader)
            {
            try
                {
                this.m_reader = reader;
                this.m_header = reader.getCSVHeader(false);
                }
            catch (IOException e)
                {
                throw new RuntimeException(e);
                }
            }

        // ---- Iterator implementation ---------------------------------

        /**
         * Returns true if there are more items to read, false otherwise.
         *
         * @return true if there are more items to read, false otherwise
         */
        public boolean hasNext()
            {
            try
                {
                m_currentLine = m_reader.read();
                }
            catch (IOException e)
                {
                throw new RuntimeException(e);
                }
            return m_currentLine != null;
            }

        /**
         * Reads the next item from the file and converts it into a map of
         * attribute names to string values.
         *
         * @return a map of attribute names to values
         */
        public Object next()
            {
            return createMap(m_header, m_currentLine);
            }

        /**
         * Not supported.
         */
        public void remove()
            {
            throw new UnsupportedOperationException(
                    "CsvIterator does not support remove operation");
            }

        // ---- helper methods ------------------------------------------

        /**
         * Creates a name-value map for a single item.
         *
         * @param keys    attribute names
         * @param values  attribute values
         *
         * @return a map of attribute names to values
         */
        private Map<String, String> createMap(String[] keys,
                                              List<String> values)
            {
            Map<String, String> mapValues = new HashMap<String, String>();
            for (int i = 0, count = values.size(); i < count; i++)
                {
                String value = values.get(i);
                mapValues.put(keys[i], value.length() > 0 ? value : null);
                }
            return mapValues;
            }

        // ---- data members --------------------------------------------

        /**
         * A list of attribute values for the last line read.
         */
        private List<String> m_currentLine;

        /**
         * Reader to use.
         */
        private ICsvListReader m_reader;

        /**
         * An array of attribute names (parsed from the header row).
         */
        private String[] m_header;
        }


    // ---- data members ----------------------------------------------------

    /**
     * Reader to use.
     */
    private Reader m_reader;

    /**
     * Preferences.
     */
    private CsvPreference m_preferences = CsvPreference.STANDARD_PREFERENCE;
    }
