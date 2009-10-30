using System.Collections;
using NUnit.Framework;
using Seovic.Coherence.Identity.Sequence;
using Tangosol.Net;

namespace Seovic.Coherence.Identity
{
    [TestFixture]
    public class SequenceGeneratorTests
    {
        private static INamedCache sequenceCache =
            CacheFactory.GetCache(SequenceGenerator.CACHE_NAME);

        [SetUp]
        public void SetUp()
        {
            sequenceCache.Clear();
        }

        [Test]
        public void TestIdGeneration()
        {
            IdentityGeneratorClient<long> igc = new IdentityGeneratorClient<long>(
                new SequenceGenerator("test"));

            Assert.AreEqual(100, igc.GenerateIdentities(1, 100).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(5, 20).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(10, 10).Count);

            Sequence.Sequence seq = (Sequence.Sequence) sequenceCache["test"];
            Assert.AreEqual("test", seq.Name);
            Assert.AreEqual(300, seq.Peek);
        }

        [Test]
        public void TestIdGenerationWithoutBlockCaching()
        {
            IdentityGeneratorClient<long> igc = new IdentityGeneratorClient<long>(
                    new SequenceGenerator("test", 1));

            Assert.AreEqual(100, igc.GenerateIdentities(5, 20).Count);

            Sequence.Sequence seq = (Sequence.Sequence)sequenceCache["test"];
            Assert.AreEqual("test", seq.Name);
            Assert.AreEqual(100, seq.Peek);
        }

        [Test]
        public void TestIdGenerationWithMultipleClients()
        {
            IdentityGeneratorClient<long> igc1 = new IdentityGeneratorClient<long>(
                    new SequenceGenerator("test", 10));
            IdentityGeneratorClient<long> igc2 = new IdentityGeneratorClient<long>(
                    new SequenceGenerator("test", 10));

            Assert.AreEqual(25, igc1.GenerateIdentities(5, 5).Count);
            Assert.AreEqual(25, igc2.GenerateIdentities(5, 5).Count);

            Sequence.Sequence seq = (Sequence.Sequence)sequenceCache["test"];
            Assert.AreEqual("test", seq.Name);
            Assert.AreEqual(60, seq.Peek);
        }
    }
}
