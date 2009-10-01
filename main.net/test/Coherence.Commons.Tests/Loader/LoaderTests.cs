using NUnit.Framework;
using Seovic.Coherence.Test.Objects;
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
    }
}