package com.seovic.coherence.loader.source;


import com.seovic.core.Extractor;
import com.seovic.core.extractor.MapExtractor;

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
 * @author ic  2009.06.09
 */
public class CsvSource
        extends AbstractBaseSource
    {

    private Reader reader;

    private CsvPreference preferences = CsvPreference.STANDARD_PREFERENCE;

    public CsvSource(Reader reader)
        {
        this.reader = reader;
        }

    public Iterator iterator()
        {
        return new CsvIterator(new CsvListReader(reader, preferences));
        }

    public void setDelimiterChar(char delimiter)
        {
        preferences.setDelimiterChar(delimiter);
        }

    public void setQuoteChar(char quote)
        {
        preferences.setQuoteChar(quote);
        }

    public void setEndOfLineSymbols(String endOfLineSymbols)
        {
        preferences.setEndOfLineSymbols(endOfLineSymbols);
        }

    protected Extractor createDefaultExtractor(String propertyName)
        {
        return new MapExtractor(propertyName);
        }

        public class CsvIterator
            implements Iterator
        {
        private List<String> currentLine;

        private ICsvListReader reader;

        private String[] header;

        public CsvIterator(ICsvListReader reader)
            {
            try
                {
                this.reader = reader;
                this.header = reader.getCSVHeader(false);
                }
            catch (IOException e)
                {
                throw new RuntimeException(e);
                }
            }

        public boolean hasNext()
            {
            try
                {
                currentLine = reader.read();
                }
            catch (IOException e)
                {
                throw new RuntimeException(e);
                }
            return currentLine != null;
            }

        public Object next()
            {
            return createMap(header, currentLine);
            }

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

        public void remove()
            {
            throw new UnsupportedOperationException(
                    "CsvIterator does not support remove operation");
            }

        }
    }
