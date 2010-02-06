using System;

namespace Seovic.Identity
{
    /// <summary>
    /// UUID-based <see cref="IIdentityGenerator" /> implementation.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.30</author>
    public class GuidGenerator : IIdentityGenerator<Guid>
    {
        /// <summary>
        /// Generates a <c>System.Guid</c> based identity.
        /// </summary>
        /// <returns>New globally unique identifier.</returns>
        public Guid GenerateIdentity()
        {
            return new Guid();
        }
    }
}
