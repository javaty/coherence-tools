
namespace Seovic.Identity
{
    /// <summary>
    /// A strategy interface for identity extractors.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.09</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public interface IIdentityExtractor<TId, TEntity>
    {
        /// <summary>
        /// Extracts identity from a specified source object.
        /// </summary>
        /// <param name="entity">Source object to extract identity from</param>
        /// <returns>Extracted identity</returns>
        TId ExtractIdentity(TEntity entity);
    }

    /// <summary>
    /// A strategy interface for identity extractors.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.09</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public interface IIdentityExtractor : IIdentityExtractor<object, object>
    {
    }
}
