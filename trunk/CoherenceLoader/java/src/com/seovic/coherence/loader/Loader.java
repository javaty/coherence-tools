package com.seovic.coherence.loader;

import com.seovic.coherence.test.objects.Country;
import com.seovic.coherence.loader.mapper.ExpressionPropertyMapper;
import com.seovic.coherence.loader.source.CsvFileSource;
import com.seovic.coherence.loader.source.CoherenceCacheSource;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;
import com.seovic.coherence.loader.target.CsvFileTarget;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.FileWriter;

/**
 * @author ic  2009.06.09
 */
public class Loader {
    private Source source;
    private Target target;

    public Loader(Source source, Target target) {
        this.source = source;
        this.target = target;
    }

    public void load() {
        target.beginImport();
        for (Object item : source) {
            target.importSingle(item);
        }
        target.endImport();
    }

    public static void main(String[] args) throws Exception {

        Reader countriesReader = new InputStreamReader(Loader.class.getClassLoader().getResourceAsStream("countries.csv"));

        Source source = new CsvFileSource(countriesReader);

        CoherenceCacheTarget target = new CoherenceCacheTarget("countries", Country.class);
        target.registerPropertyMapper("name", new ExpressionPropertyMapper("code + ':' + ime.toUpperCase()", null));

        Loader loader = new Loader(source, target);
        loader.load();
        
        NamedCache cache = CacheFactory.getCache("countries");
        System.out.println("Imported " + cache.size() + " items.");
        System.out.println(cache.get("SRB"));
        System.out.println(cache.get("ESH"));

        Source source2 = new CoherenceCacheSource("countries");

        Writer writer = new FileWriter("countries-out.csv", false);
        CsvFileTarget target2 = new CsvFileTarget(writer, "code", "name", "domain");
        target2.registerPropertyMapper("name", new ExpressionPropertyMapper("code + ':' + name.toLowerCase()", null));

        Loader loader2 = new Loader(source2, target2);
        loader2.load();
        
    }
}
