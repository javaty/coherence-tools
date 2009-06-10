package com.seovic.coherence.loader.target;

import org.supercsv.io.ICsvMapWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.seovic.coherence.loader.PropertyMapper;

/**
 * @author ic  2009.06.09
 */
public class CsvTarget extends AbstractBaseTarget {
    private ICsvMapWriter writer;
    private String[] propertyNames;

    public CsvTarget(Writer writer, String propertyNames) {
        this(writer, propertyNames.split(","));
    }

    public CsvTarget(Writer writer, String... propertyNames) {
        this.writer = new CsvMapWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        this.propertyNames = propertyNames;
    }

    public void beginImport() {
        try {
            writer.writeHeader(propertyNames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importSingle(Object item) {
        try {
            writer.write(map(item), propertyNames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void endImport() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(Object source) {
       Map<String, Object> target = new HashMap<String, Object>();
       for (String propertyName : propertyNames) {
            PropertyMapper pm = getPropertyMapper(propertyName);
            target.put(propertyName, pm.getValue(source));
        }
        return target;
    }
    
}
