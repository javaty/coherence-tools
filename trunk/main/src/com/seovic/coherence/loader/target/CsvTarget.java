package com.seovic.coherence.loader.target;


import com.seovic.coherence.loader.Source;

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
 * @author ic  2009.06.09
 */
public class CsvTarget
        extends AbstractBaseTarget
    {
    private ICsvMapWriter writer;

    private String[] propertyNames;

    public CsvTarget(Writer writer, String propertyNames)
        {
        this(writer, propertyNames.split(","));
        }

    public CsvTarget(Writer writer, String... propertyNames)
        {
        this.writer = new CsvMapWriter(writer,
                                       CsvPreference.STANDARD_PREFERENCE);
        this.propertyNames = propertyNames;
        }

    protected Updater createDefaultUpdater(String propertyName)
        {
        return new MapUpdater(propertyName);
        }

    public void beginImport()
        {
        try
            {
            writer.writeHeader(propertyNames);
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        }

    @SuppressWarnings({"unchecked"})
    public void importItem(Object item)
        {
        try
            {
            writer.write((Map<String, ?>) item, propertyNames);
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }

        }

    public void endImport()
        {
        try
            {
            writer.close();
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        }

    public String[] getPropertyNames()
        {
        return propertyNames;
        }

    public Object createTargetInstance(Source source, Object sourceItem)
        {
        return new HashMap<String, Object>();
        }
    }
