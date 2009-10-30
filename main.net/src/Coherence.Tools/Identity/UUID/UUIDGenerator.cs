
namespace Seovic.Coherence.Identity.UUID
{
    /// <summary>
    /// UUID-based <see cref="IIdentityGenerator" /> implementation.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.30</author>
    public class UUIDGenerator : IIdentityGenerator<Tangosol.Util.UUID>
    {
        /// <summary>
        /// Generates a UUID based identity.
        /// </summary>
        /// <returns>New UUID value</returns>
        public Tangosol.Util.UUID GenerateIdentity()
        {
            return new Tangosol.Util.UUID();
        }
    }
}
