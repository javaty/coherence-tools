/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

using System;

using NUnit.Framework;

using Seovic.Coherence.Test.Objects;

using Tangosol.IO.Pof;
using Tangosol.Net.Cache;
using Tangosol.Net.Cache.Support;
using Tangosol.Util;
using Tangosol.Util.Extractor;

namespace Seovic.Coherence.Util.Extractor
{
    /// <summary>
    /// Tests for <c>PropertyExtractor</c> class.
    /// </summary>
    [TestFixture]
    public class PropertyExtractorTests
    {
        [Test]
        public void TestExtraction()
        {
            Country serbia = new Country("SRB", "Serbia");
            IValueExtractor codeEx = new PropertyExtractor("Code");
            IValueExtractor nameEx = new PropertyExtractor("Name");

            Assert.AreEqual("SRB", codeEx.Extract(serbia));
            Assert.AreEqual("Serbia", nameEx.Extract(serbia));
        }

        [Test]
        public void TestValueExtractionFromEntry()
        {
            ICacheEntry entry = new CacheEntry(null, new Country("SRB", "Serbia"));

            PropertyExtractor codeEx = new PropertyExtractor("code");
            PropertyExtractor nameEx = new PropertyExtractor("name", PropertyExtractor.VALUE);

            Assert.AreEqual("SRB", InvocableCacheHelper.ExtractFromEntry(codeEx, entry));
            Assert.AreEqual("Serbia", InvocableCacheHelper.ExtractFromEntry(nameEx, entry));
        }

        [Test]
        public void TestKeyExtractionFromEntry()
        {
            ICacheEntry entry = new CacheEntry(new Country("SRB", "Serbia"), null);

            IValueExtractor codeEx   = new KeyExtractor(new PropertyExtractor("code"));
            PropertyExtractor nameEx = new PropertyExtractor("name", PropertyExtractor.KEY);

            Assert.AreEqual("SRB", InvocableCacheHelper.ExtractFromEntry(codeEx, entry));
            Assert.AreEqual("Serbia", InvocableCacheHelper.ExtractFromEntry(nameEx, entry));
        }

        [Test]
        public void TestNullTarget()
        {
            IValueExtractor ex = new PropertyExtractor("code");
            Assert.IsNull(ex.Extract(null));
        }

        [Test]
        [ExpectedException(typeof (Exception))]
        public void TestMissingProperty()
        {
            Country serbia = new Country("SRB", "Serbia");
            new PropertyExtractor("xyz").Extract(serbia);
        }

        [Test]
        public void TestPofSerialization()
        {
            SimplePofContext ctx = new SimplePofContext();
            ctx.RegisterUserType(1, typeof (PropertyExtractor), new PortableObjectSerializer(1));

            Object original = new PropertyExtractor("xyz");
            Binary bin      = SerializationHelper.ToBinary(original, ctx);
            Object copy     = SerializationHelper.FromBinary(bin, ctx);

            Assert.AreEqual(original, copy);
            Assert.AreEqual(original.GetHashCode(), copy.GetHashCode());
            Assert.AreEqual(original.ToString(), copy.ToString());
        }

        [Test]
        public void TestEquals()
        {
            Object o = new PropertyExtractor("xyz");

            Assert.IsTrue( o.Equals(o));
            Assert.IsFalse(o.Equals(null));
            Assert.IsFalse(o.Equals("invalid class"));
            Assert.IsFalse(o.Equals(new PropertyExtractor("abc")));
        }
    }
}