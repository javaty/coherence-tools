
using Seovic.Coherence.Core;
using Tangosol.Util.Extractor;

namespace Seovic.Coherence.Identity.Extractor
{
    /// <summary>
    /// An <see cref="IdentityExtractor" /> that can be used with the classes implementing
    /// <see cref="IEntity{TId}" /> interface.
    /// </summary>
    /// <typeparam name="T">Type of identifier.</typeparam>
    public class EntityIdentityExtractor<T> : IIdentityExtractor<T, IEntity<T>>
    {
        /// <summary>
        /// Extracts identity from a specified source object.
        /// </summary>
        /// <param name="entity">Source object to extract identity from</param>
        /// <returns>Extracted identity</returns>
        /// <summary>
        /// Extracts identity from a specified source object.
        /// </summary>
        /// <param name="entity">Source object to extract identity from</param>
        /// <returns>Extracted identity</returns>
        public T ExtractIdentity(IEntity<T> entity)
        {
            return entity.Id;
        }
    }

    /// <summary>
    /// An <see cref="IdentityExtractor" /> that can be used with the classes implementing
    /// <see cref="IEntity{TId}" /> interface.
    /// </summary>
    public class EntityIdentityExtractor : IIdentityExtractor
    {
        /// <summary>
        /// Extracts identity from a specified source object.
        /// </summary>
        /// <param name="entity">Source object to extract identity from</param>
        /// <returns>Extracted identity</returns>
        /// <summary>
        /// Extracts identity from a specified source object.
        /// </summary>
        /// <param name="entity">Source object to extract identity from</param>
        /// <returns>Extracted identity</returns>
        public object ExtractIdentity(object entity)
        {
            return ((IEntity<object>) entity).Id;
        }
    }
}