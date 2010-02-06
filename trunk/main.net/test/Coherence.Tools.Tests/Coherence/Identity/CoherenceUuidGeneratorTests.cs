using NUnit.Framework;
using Seovic.Identity;

namespace Seovic.Coherence.Identity
{
    [TestFixture]
    public class CoherenceUuidGeneratorTests
    {
        [Test]
        public void TestIdGeneration()
        {
            IdentityGeneratorClient<Tangosol.Util.UUID> igc = new IdentityGeneratorClient<Tangosol.Util.UUID>(
                new CoherenceUuidGenerator());

            Assert.AreEqual(100, igc.GenerateIdentities(1, 100).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(5, 20).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(10, 10).Count);
        }
    }
}