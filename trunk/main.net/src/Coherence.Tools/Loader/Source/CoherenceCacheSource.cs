using System.Collections;
using Seovic.Coherence.Core;
using Tangosol.Net;

namespace Seovic.Coherence.Loader.Source
{
    /// <summary>
    /// A <see cref="ISource"/> implementation that reads items to load from a Coherence
    /// cache.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public class CoherenceCacheSource : AbstractBaseSource
    {
        #region Constructors

        /// <summary>
        /// Construct a CoherenceCacheSource instance.
        /// </summary>
        /// <param name="cacheName">Cache to read objects from.</param>
        public CoherenceCacheSource(string cacheName)
        {
            cache = CacheFactory.GetCache(cacheName);
        }

        public CoherenceCacheSource(INamedCache cache)
        {
            this.cache = cache;
        }

        #endregion

        #region IEnumerable implementation

        /// <summary>
        /// Returns an enumerator that iterates through a collection.
        /// </summary>
        /// <returns>
        /// An <see cref="T:System.Collections.IEnumerator"/> object that can be used to iterate through the collection.
        /// </returns>
        public override IEnumerator GetEnumerator()
        {
            return cache.Values.GetEnumerator();
        }

        #endregion

        #region AbstractBaseSource implementation

        /// <summary>
        /// Create default extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property to create an extractor for</param>
        /// <returns>Property extractor instance</returns>
        protected override IExtractor CreateDefaultExtractor(string propertyName)
        {
            return Defaults.CreateExtractor(propertyName);
        }

        #endregion

        #region Data members

        /// <summary>
        /// Cache to read objects from.
        /// </summary>
        private INamedCache cache;

        #endregion
    }
}