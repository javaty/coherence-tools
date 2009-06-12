package com.seovic.coherence.loader;

import com.seovic.coherence.loader.source.CsvSource;
import com.seovic.coherence.loader.target.AbstractBaseTarget;
import com.seovic.coherence.loader.target.CoherenceCacheTarget;
import com.seovic.coherence.test.objects.Country;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

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

        Source source = new CsvSource(countriesReader);


        CoherenceCacheTarget target = new CoherenceCacheTarget("countries", Country.class);
        //target.registerPropertyMapper("name", new ExpressionPropertyMapper("code + ':' + ime.toUpperCase()"));
//        Writer writer = new FileWriter("countries-out.xml", false);
//        Map<String, String> nmsp = new HashMap<String, String>();
//        nmsp.put("v", "validationUri");
//        nmsp.put(null, "defaultNs");
//        nmsp.put("c", "jstlUri");
//        nmsp.put("config", "sigfeConfigUri");
//        nmsp.put("id", "idUri");
//
//        XmlTarget target = new XmlTarget(writer, nmsp,  "countries", "country", "id:@code,@name,config:formalName,config:capital,currencySymbol,currencyName,v:telephonePrefix,v:domain");
//        target.registerPropertyMapper("name", new ExpressionPropertyMapper("ime"));


        Loader loader = new Loader(source,target);
        loader.load();

        NamedCache cache = CacheFactory.getCache("countries");
        System.out.println("Imported " + cache.size() + " items.");
        System.out.println(cache.get("SRB"));
        System.out.println(cache.get("ESH"));
//
//        Source source2 = new CoherenceCacheSource("countries");

//        Writer writer = new FileWriter("countries-out.xml", false);
//        XmlTarget target2 = new XmlTarget(writer, "countries", "country", "code,name,formalName,capital,currencySymbol,currencyName,telephonePrefix,domain");
//        target2.registerPropertyMapper("name", new ExpressionPropertyMapper("code + ':' + name.toLowerCase()", null));
//
//        Loader loader2 = new Loader(source2, target2);
//        loader2.load();

    }

    public static class XmlReporterTarget extends AbstractBaseTarget {

        public void importSingle(Object item) {
            try {
                DOMSource domSource = new DOMSource((Document) item);
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result);
                System.out.println(writer.toString());
            }
            catch (TransformerException ignore) {
            }
        }
    }


}

