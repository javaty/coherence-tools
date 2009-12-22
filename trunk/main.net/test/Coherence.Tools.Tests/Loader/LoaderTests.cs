using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using NUnit.Framework;
using Seovic.Coherence.Core.Extractor;
using Seovic.Coherence.Loader.Source;
using Seovic.Coherence.Loader.Target;
using Seovic.Coherence.Test.Objects;
using Seovic.Coherence.Util;
using Tangosol.Net;

namespace Seovic.Coherence.Loader
{
    [TestFixture]
    public class LoaderTests
    {
        public static readonly INamedCache countries = CacheFactory.GetCache("countries");

        [SetUp]
        public void ClearCache()
        {
            countries.Clear();
        }

        [Test]
        public void TestCsvToCoherenceLoader()
        {
            ILoader loader = new CsvToCoherence("Resources/countries.csv", countries, typeof(Country));
            loader.Load();

            // asserts
            Assert.AreEqual(244, countries.Count);

            Country srb = (Country) countries["SRB"];
            Assert.AreEqual("Serbia", srb.Name);
            Assert.AreEqual("Belgrade", srb.Capital);
            Assert.AreEqual("RSD", srb.CurrencySymbol);
        }

        [Test]
        public void TestXmlToCoherenceLoader()
        {
            ISource source = new XmlSource("Resources/countries.xml");
            
            ITarget target = new CoherenceCacheTarget(countries, typeof(Country));
            ILoader loader = new DefaultLoader(source, target);
            loader.Load();

            // asserts
            Assert.AreEqual(244, countries.Count);

            Country srb = (Country)countries["SRB"];
            Assert.AreEqual("Republic of Serbia", srb.FormalName);
            Assert.AreEqual("Belgrade", srb.Capital);
            Assert.AreEqual("RSD", srb.CurrencySymbol);
        }

        [Test]
        public void TestXmlWithNamespacesToCoherenceLoader()
        {
            ISource source = new XmlSource("Resources/countries-ns.xml");
            source.SetExtractor("code", new XmlExtractor("code", "http://schemas.seovic.com/id"));
            source.SetExtractor("formalName", new XmlExtractor("formalName", "http://schemas.seovic.com/config"));
            source.SetExtractor("capital", new XmlExtractor("capital", "http://schemas.seovic.com/config"));
            source.SetExtractor("telephonePrefix", new XmlExtractor("telephonePrefix", "http://schemas.seovic.com/validation"));
            source.SetExtractor("domain", new XmlExtractor("domain", "http://schemas.seovic.com/validation"));

            ITarget target = new CoherenceCacheTarget(countries, typeof(Country));
            ILoader loader = new DefaultLoader(source, target);
            loader.Load();

            // asserts
            Assert.AreEqual(244, countries.Count);

            Country srb = (Country)countries["SRB"];
            Assert.AreEqual("Serbia", srb.Name);
            Assert.AreEqual("Belgrade", srb.Capital);
            Assert.AreEqual("RSD", srb.CurrencySymbol);
        }

        [Test]
        public void TestCoherenceToXmlLoader()
        {
            PrepareCache();

            ISource source = new CoherenceCacheSource(countries);
            StringWriter writer = new StringWriter();
            ITarget target = new XmlTarget(writer, "countries", "country",
                                      "@code,name,formalName,capital,currencySymbol,currencyName,telephonePrefix,domain");

            ILoader loader = new DefaultLoader(source, target);
            loader.Load();

            XmlDocument result = new XmlDocument();
            result.Load(new StringReader(writer.ToString()));

            XmlElement root = result.DocumentElement;
            Console.WriteLine(root.InnerXml);
            XmlNodeList countryList = root.GetElementsByTagName("country");

            Assert.AreEqual(3, countryList.Count);

            IList<string> expectedCodes = new List<string>(3);
            expectedCodes.Add("CHL");
            expectedCodes.Add("SRB");
            expectedCodes.Add("SGP");

            IList<string> actualCodes = new List<string>(3);
            foreach(XmlNode node in countryList)
            {
                string actual = ((XmlElement) node).GetAttribute("code");
                Assert.IsTrue(expectedCodes.Contains(actual));
            }
        }

        [Test]
        public void TestCoherenceToXmlWithNamespacesLoader()
        {
            PrepareCache();

            ISource source = new CoherenceCacheSource(countries);
            StringWriter writer = new StringWriter();
            IDictionary<string, string> nsmap = new Dictionary<string, string>();
            nsmap.Add(string.Empty, "http://schemas.seovic.com/default");
            nsmap.Add("id", "http://schemas.seovic.com/id");
            nsmap.Add("config", "http://schemas.seovic.com/config");
            nsmap.Add("v", "http://schemas.seovic.com/validation");

            ITarget target = new XmlTarget(writer, nsmap, "countries", "country",
                                      "id:@code,name,formalName,capital,config:currencySymbol,config:currencyName,v:telephonePrefix,v:domain");

            ILoader loader = new DefaultLoader(source, target);
            loader.Load();

            XmlDocument result = new XmlDocument();
            result.Load(new StringReader(writer.ToString()));

            XmlElement root = result.DocumentElement;
            Console.WriteLine(root.OuterXml);
            XmlNodeList countryList = root.GetElementsByTagName("country");

            Assert.AreEqual(3, countryList.Count);

            IList<string> expectedCodes = new List<string>(3);
            expectedCodes.Add("CHL");
            expectedCodes.Add("SRB");
            expectedCodes.Add("SGP");

            foreach (XmlNode node in countryList)
            {
                string actual = ((XmlElement)node).GetAttribute("code", nsmap["id"]);
                Assert.IsTrue(expectedCodes.Contains(actual));
            }
        }


        protected static void PrepareCache()
        {
            countries.Insert("SRB", CreateCountry(
                "SRB,Serbia,Republic of Serbia,Belgrade,RSD,Dinar,+381,.rs and .yu"));
            countries.Insert("SGP", CreateCountry(
                "SGP,Singapore,Republic of Singapore,Singapore,SGD,Dollar,+65,.sg"));
            countries.Insert("CHL", CreateCountry(
                "CHL,Chile,Republic of Chile,Santiago (administrative/judical) and Valparaiso (legislative),CLP,Peso,+56,.cl"));
        }

        protected static Country CreateCountry(string props)
        {
            string[] properties = props.Split(',');
            Country c = new Country();
            c.Code = properties[0];
            c.Name = properties[1];
            c.FormalName = properties[2];
            c.Capital = properties[3];
            c.CurrencySymbol = properties[4];
            c.CurrencyName = properties[5];
            c.TelephonePrefix = properties[6];
            c.Domain = properties[7];
            return c;
        }
    }

}