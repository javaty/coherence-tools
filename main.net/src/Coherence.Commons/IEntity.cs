namespace Seovic.Coherence
{
    /// <summary>
    /// An interface that should be implemented by all entities.
    /// </summary>
    /// <typeparam name="T">Identifier type.</typeparam>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    public interface IEntity<T>
    {
        /// <summary>
        /// Return entity identifier.
        /// </summary>
        /// <value>Entity identifier.</value>
        T Id { get; }
    }
}