package com.seovic.coherence.loader.source;

import com.seovic.coherence.loader.Source;

import java.util.Iterator;
import java.util.Map;

import java.io.Reader;
import java.io.IOException;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;

import org.supercsv.prefs.CsvPreference;

/**
 * @author ic  2009.06.09
 */
public class CsvSource implements Source {

    private Reader reader;
    private CsvPreference preferences = CsvPreference.STANDARD_PREFERENCE;

    public CsvSource(Reader reader) {
        this.reader = reader;
    }

    public Iterator iterator() {
        return new CsvIterator(new CsvMapReader(reader, preferences));
    }

    public void setDelimiterChar(char delimiter) {
        preferences.setDelimiterChar(delimiter);
    }

    public void setQuoteChar(char quote) {
        preferences.setQuoteChar(quote);  
    }

    public void setEndOfLineSymbols(String endOfLineSymbols) {
        preferences.setEndOfLineSymbols(endOfLineSymbols);  
    }

    public class CsvIterator implements Iterator {
        private Map<String, String> currentLine;
        private ICsvMapReader reader;
        private String[]      header;

        public CsvIterator(ICsvMapReader reader) {
            try {
                this.reader = reader;
                this.header = reader.getCSVHeader(false);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            try {
                currentLine = reader.read(header);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return currentLine != null;
        }

        public Object next() {
            return currentLine;
        }

        public void remove() {
            throw new UnsupportedOperationException("CsvIterator does not support remove operation");
        }

    }
}
