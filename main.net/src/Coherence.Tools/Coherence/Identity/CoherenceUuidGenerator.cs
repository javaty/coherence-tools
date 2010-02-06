using Seovic.Identity;
using Tangosol.Util;

namespace Seovic.Coherence.Identity
{
    /// <summary>
    /// Coherence UUID-based <see cref="IIdentityGenerator" /> implementation.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.30</author>
    public class CoherenceUuidGenerator : IIdentityGenerator<UUID>
    {
        /// <summary>
        /// Generates a UUID based identity.
        /// </summary>
        /// <returns>New UUID value</returns>
        public UUID GenerateIdentity()
        {
            return new UUID();
        }
    }
}