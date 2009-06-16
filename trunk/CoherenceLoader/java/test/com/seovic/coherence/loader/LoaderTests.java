package com.seovic.coherence.loader;


import com.seovic.coherence.loader.properties.ExpressionPropertyGetter;
import com.seovic.coherence.loader.properties.XmlPropertyGetter;
import com.seovic.coherence.loader.source.CoherenceCacheSource;
import com.seovic.coherence.loader.source.CsvSource;
import com.seovic.coherence.loader.source.XmlSource;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;
import com.seovic.coherence.loader.target.CsvTarget;
import com.seovic.coherence.loader.target.XmlTarget;
import com.seovic.coherence.test.objects.Country;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilderFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * @author ic  2009.06.15
 */
public class LoaderTests
    {

    public static final NamedCache countriesCache = CacheFactory.getCache(
            "countries");

    @Before
    public void clearCache()
        {
        countriesCache.clear();
        }

    @Test
    public void testCsvToCoherenceLoader()
        {
        Reader countriesReader = new InputStreamReader(
                Loader.class.getClassLoader().getResourceAsStream(
                        "countries.csv"));
        Source source = new CsvSource(countriesReader);
        Target target = new CoherenceCacheTarget("countries", Country.class);
        Loader loader = new Loader(source, target);
        loader.load();

        // asserts
        assertEquals(244, countriesCache.size());

        Country srb = (Country) countriesCache.get("SRB");
        assertEquals("Serbia", srb.getName());
        assertEquals("Belgrade", srb.getCapital());
        assertEquals("RSD", srb.getCurrencySymbol());
        }

    @Test
    public void testCoherenceToCsvLoader()
            throws IOException
        {
        prepareCache();
        Source source = new CoherenceCacheSource("countries");
        Writer writer = new StringWriter();
        Target target = new CsvTarget(writer,
                                      "code,formalName,capital,currencySymbol,currencyName,telephonePrefix,domain");

        Loader loader = new Loader(source, target);
        loader.load();

        // asserts
        BufferedReader reader = new BufferedReader(new StringReader(
                writer.toString()));
        int count = 0;
        while (reader.readLine() != null)
            {
            count++;
            }
        assertEquals(4, count);
        }

    @Test
    public void testXmlToCoherenceLoader()
        {
        Reader countriesReader = new InputStreamReader(
                Loader.class.getClassLoader().getResourceAsStream(
                        "countries.xml"));
        Source source = new XmlSource(countriesReader);

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
    public void testXmlWithNamespacesToCoherenceLoader()
        {
        Reader countriesReader = new InputStreamReader(
                Loader.class.getClassLoader().getResourceAsStream(
                        "countries-ns.xml"));
        Source source = new XmlSource(countriesReader);
        source.setPropertyGetter("code", new XmlPropertyGetter("code",
                                                               "http://schemas.seovic.com/id"));
        source.setPropertyGetter("formalName", new XmlPropertyGetter(
                "formalName", "http://schemas.seovic.com/config"));
        source.setPropertyGetter("capital", new XmlPropertyGetter("capital",
                                                                  "http://schemas.seovic.com/config"));
        source.setPropertyGetter("telephonePrefix", new XmlPropertyGetter(
                "telephonePrefix", "http://schemas.seovic.com/validation"));
        source.setPropertyGetter("domain", new XmlPropertyGetter("domain",
                                                                 "http://schemas.seovic.com/validation"));

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
    public void testCoherenceToXmlLoader()
            throws Exception
        {
        prepareCache();
        Source source = new CoherenceCacheSource("countries");
        StringWriter writer = new StringWriter();
        Target target = new XmlTarget(writer, "countries", "country",
                                      "@code,name,formalName,capital,currencySymbol,currencyName,telephonePrefix,domain");

        Loader loader = new Loader(source, target);
        loader.load();

        System.out.println(writer.toString());
        // asserts
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().parse(
                new InputSource(new StringReader(writer.toString())));

        Element root = doc.getDocumentElement();
        NodeList countries = root.getElementsByTagName("country");

        assertEquals(3, countries.getLength());
        assertEquals("SGP", ((Element) countries.item(0)).getAttribute("code"));
        assertEquals("CHL", ((Element) countries.item(1)).getAttribute("code"));
        assertEquals("SRB", ((Element) countries.item(2)).getAttribute("code"));
        }

    @Test
    public void testCsvToCoherenceWithExpressionsLoader()
        {
        Reader countriesReader = new InputStreamReader(
                Loader.class.getClassLoader().getResourceAsStream(
                        "countries.csv"));
        Source source = new CsvSource(countriesReader);
        Target target = new CoherenceCacheTarget("countries", Country.class);
        Loader loader = new Loader(source, target);
        source.setPropertyGetter("name", new ExpressionPropertyGetter(
                "code + ':' + name+ ':' + formalName"));
        loader.load();

        // asserts
        assertEquals(244, countriesCache.size());

        Country srb = (Country) countriesCache.get("SRB");
        assertEquals("SRB:Serbia:Republic of Serbia", srb.getName());
        }


    @SuppressWarnings({"unchecked"})
    protected static void prepareCache()
        {
        countriesCache.put("SRB", createCountry(
                "SRB,Serbia,Republic of Serbia,Belgrade,RSD,Dinar,+381,.rs and .yu"));
        countriesCache.put("SGP", createCountry(
                "SGP,Singapore,Republic of Singapore,Singapore,SGD,Dollar,+65,.sg"));
        countriesCache.put("CHL", createCountry(
                "CHL,Chile,Republic of Chile,Santiago (administrative/judical) and Valparaiso (legislative),CLP,Peso,+56,.cl"));
        }

    protected static Country createCountry(String properties)
        {
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
