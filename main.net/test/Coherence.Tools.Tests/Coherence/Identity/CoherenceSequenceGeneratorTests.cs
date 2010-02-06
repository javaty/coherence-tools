using NUnit.Framework;
using Seovic.Identity;
using Tangosol.Net;

namespace Seovic.Coherence.Identity
{
    [TestFixture]
    public class CoherenceSequenceGeneratorTests
    {
        private static readonly INamedCache sequenceCache =
            CacheFactory.GetCache(CoherenceSequenceGenerator.CACHE_NAME);

        [SetUp]
        public void SetUp()
        {
            sequenceCache.Clear();
        }

        [Test]
        public void TestIdGeneration()
        {
            IdentityGeneratorClient<long> igc = new IdentityGeneratorClient<long>(
                new CoherenceSequenceGenerator("test", 20));

            Assert.AreEqual(100, igc.GenerateIdentities(1, 100).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(5, 20).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(10, 10).Count);

            SequenceGenerator.Sequence seq = (SequenceGenerator.Sequence) sequenceCache["test"];
            Assert.AreEqual("test", seq.Name);
            Assert.AreEqual(300, seq.Last);
        }

        [Test]
        public void TestIdGenerationWithoutBlockCaching()
        {
            IdentityGeneratorClient<long> igc = new IdentityGeneratorClient<long>(
                new CoherenceSequenceGenerator("test", 1));

            Assert.AreEqual(100, igc.GenerateIdentities(5, 20).Count);

            SequenceGenerator.Sequence seq = (SequenceGenerator.Sequence) sequenceCache["test"];
            Assert.AreEqual("test", seq.Name);
            Assert.AreEqual(100, seq.Last);
        }

        [Test]
        public void TestIdGenerationWithMultipleClients()
        {
            IdentityGeneratorClient<long> igc1 = new IdentityGeneratorClient<long>(
                new CoherenceSequenceGenerator("test", 10));
            IdentityGeneratorClient<long> igc2 = new IdentityGeneratorClient<long>(
                new CoherenceSequenceGenerator("test", 10));

            Assert.AreEqual(25, igc1.GenerateIdentities(5, 5).Count);
            Assert.AreEqual(25, igc2.GenerateIdentities(5, 5).Count);

            SequenceGenerator.Sequence seq = (SequenceGenerator.Sequence) sequenceCache["test"];
            Assert.AreEqual("test", seq.Name);
            Assert.AreEqual(60, seq.Last);
        }
    }
}