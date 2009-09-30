using NUnit.Framework;
using Seovic.Coherence.Identity.UUID;

namespace Seovic.Coherence.Identity
{
    [TestFixture]
    public class UUIDGeneratorTests
    {
        [Test]
        public void TestIdGeneration()
        {
            IdentityGeneratorClient<Tangosol.Util.UUID> igc = new IdentityGeneratorClient<Tangosol.Util.UUID>(
                new UUIDGenerator());

            Assert.AreEqual(100, igc.GenerateIdentities(1, 100).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(5, 20).Count);
            Assert.AreEqual(100, igc.GenerateIdentities(10, 10).Count);
        }
    }
}