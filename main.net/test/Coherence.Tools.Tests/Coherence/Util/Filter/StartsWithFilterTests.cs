using System;
using System.Collections;
using NUnit.Framework;

using Seovic.Core.Extractor;
using Seovic.Test.Objects;

using Tangosol.IO.Pof;
using Tangosol.Net.Cache;
using Tangosol.Util;

namespace Seovic.Coherence.Util.Filter
{
    [TestFixture]
    public class StartsWithFilterTests
    {
        private static readonly LocalCache countries = new LocalCache();

        [SetUp]
        public void CreateTestData()
        {
            countries.Insert("SRB", new Country("SRB", "Serbia"));
            countries.Insert("SUI", new Country("SUI", "Switzerland"));
            countries.Insert("SWE", new Country("SWE", "Sweden"));
            countries.Insert("ESP", new Country("ESP", "Spain"));
            countries.Insert("USA", new Country("USA", "United States"));

            countries.AddIndex(new PropertyExtractor("name"), true, null);
        }

        [Test]
        public void TestCaseSensitiveEvaluation()
        {
            Country country = new Country("SRB", "Serbia");

            Assert.IsTrue(new StartsWithFilter("name", "Ser", false).Evaluate(country));
            Assert.IsFalse(new StartsWithFilter("name", "ser", false).Evaluate(country));
            Assert.IsFalse(new StartsWithFilter("name", "SER", false).Evaluate(country));
            Assert.IsFalse(new StartsWithFilter("name", "Srb", false).Evaluate(country));
        }

        [Test]
        public void TestCaseInsensitiveEvaluation()
        {
            Country country = new Country("SRB", "Serbia");

            Assert.IsTrue(new StartsWithFilter("name", "Ser", true).Evaluate(country));
            Assert.IsTrue(new StartsWithFilter("name", "ser", true).Evaluate(country));
            Assert.IsTrue(new StartsWithFilter("name", "SER", true).Evaluate(country));
            Assert.IsFalse(new StartsWithFilter("name", "Srb", true).Evaluate(country));
        }

        [Test]
        public void testCaseSensitiveEvaluationWithIndex()
        {
            IFilter filter = new StartsWithFilter("name", "Sw", false);

            ArrayList keys = new ArrayList(countries.GetKeys(filter));
            Assert.AreEqual(2, keys.Count);
            Assert.IsTrue(keys.Contains("SUI"));
            Assert.IsTrue(keys.Contains("SWE"));
        }

        [Test]
        public void testCaseInsensitiveEvaluationWithIndex()
        {
            IFilter filter = new StartsWithFilter("name", "s", true);

            ArrayList keys = new ArrayList(countries.GetKeys(filter));
            Assert.AreEqual(4, keys.Count);
            Assert.IsTrue(keys.Contains("SRB"));
            Assert.IsTrue(keys.Contains("SUI"));
            Assert.IsTrue(keys.Contains("SWE"));
            Assert.IsTrue(keys.Contains("ESP"));
        }

        [Test]
        public void testEvaluationWithMissingIndex()
        {
            IFilter filter = new StartsWithFilter("code", "S", false);

            ArrayList keys = new ArrayList(countries.GetKeys(filter));
            Assert.AreEqual(3, keys.Count);
            Assert.IsTrue(keys.Contains("SRB"));
            Assert.IsTrue(keys.Contains("SUI"));
            Assert.IsTrue(keys.Contains("SWE"));
        }

        [Test]
        public void testPofSerialization()
        {
            SimplePofContext ctx = new SimplePofContext();
            ctx.RegisterUserType(1, typeof (StartsWithFilter), new PortableObjectSerializer(1));
            ctx.RegisterUserType(2, typeof (PropertyExtractor), new PortableObjectSerializer(2));

            Object original = new StartsWithFilter("name", "s", false);
            Binary binary = SerializationHelper.ToBinary(original, ctx);
            Object copy = SerializationHelper.FromBinary(binary, ctx);

            Assert.AreEqual(original, copy);
            Assert.AreEqual(original.GetHashCode(), copy.GetHashCode());
            Assert.AreEqual(original.ToString(), copy.ToString());
        }

        [Test]
        public void testEquals()
        {
            Object o = new StartsWithFilter("name", "s", true);

            Assert.IsTrue(o.Equals(o));
            Assert.IsFalse(o.Equals(null));
            Assert.IsFalse(o.Equals("invalid class"));
            Assert.IsFalse(o.Equals(new StartsWithFilter("code", "S", true)));
        }
    }
}