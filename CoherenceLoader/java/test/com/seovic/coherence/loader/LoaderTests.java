package com.seovic.coherence.loader;

import com.seovic.coherence.loader.source.CsvSource;
import com.seovic.coherence.loader.source.CoherenceCacheSource;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;
import com.seovic.coherence.loader.target.CsvTarget;
import com.seovic.coherence.test.objects.Country;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.io.*;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * @author ic  2009.06.15
 */
public class LoaderTests {

    public static final NamedCache countriesCache = CacheFactory.getCache("countries");

    @Before
    public void clearCache() {
       countriesCache.clear();
    }

    @Test
    public void testCsvToCoherenceLoader() {
        Reader countriesReader = new InputStreamReader(Loader.class.getClassLoader().getResourceAsStream("countries.csv"));
        Source source = new CsvSource(countriesReader);
        Target target = new CoherenceCacheTarget("countries", Country.class);
        Loader loader = new Loader(source, target);
        loader.load();

        // asserts
        assertEquals(244, countriesCache.size());

        Country srb = (Country) countriesCache.get("SRB");
        assertEquals("Belgrade", srb.getCapital());
        assertEquals("RSD", srb.getCurrencySymbol());
    }

    @Test
    public void testCoherenceToCsvLoader() throws IOException {
        prepareCache();
        Source source = new CoherenceCacheSource("countries");
        Writer countriesWriter = new FileWriter("countries-out.csv", false);
        Target target = new CsvTarget(countriesWriter, "code,formalName,capital,currencySymbol,currencyName,telephonePrefix,domain");

        Loader loader = new Loader(source, target);
        loader.load();

        // asserts
        BufferedReader reader = new BufferedReader(new FileReader("countries-out.csv"));
        int count = 0;
        while (reader.readLine()!=null) count++;
        assertEquals(4, count);
    }

    @SuppressWarnings({"unchecked"})
    protected static void prepareCache() {
        countriesCache.put("SRB", createCountry("SRB,Serbia,Republic of Serbia,Belgrade,RSD,Dinar,+381,.rs and .yu"));
        countriesCache.put("SGP", createCountry("SGP,Singapore,Republic of Singapore,Singapore,SGD,Dollar,+65,.sg"));
        countriesCache.put("CHL", createCountry("CHL,Chile,Republic of Chile,Santiago (administrative/judical) and Valparaiso (legislative),CLP,Peso,+56,.cl"));
    }

    protected static Country createCountry(String properties) {
        String[] values = properties.split(",");
        Country c = new Country();
        c.setCode(values[0]);
        c.setName(values[1]);
        c.setFormalName(values[2]);
        c.setCapital(values[3]);
        c.setCurrencySymbol(values[4]);
        c.setCurrencyName(values[5]);
        c.setTelephonePrefix(values[6]);
        c.setDomain(values[7]);
        return c;
    }
}
