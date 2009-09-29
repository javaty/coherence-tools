namespace Seovic.Coherence.Core
{
    /// <summary>
    /// An interface that should be implemented by all entities.
    /// </summary>
    /// <typeparam name="TId">Type of identifier.</typeparam>
    public interface IEntity<TId>
    {
        /// <summary>
        /// Return object identifier.
        /// </summary>
        TId Id
        {
            get;
        }
    }
}
