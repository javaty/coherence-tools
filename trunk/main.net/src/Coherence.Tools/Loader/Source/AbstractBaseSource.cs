using System;
using System.Collections;
using System.Collections.Generic;
using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader.Source
{
    /// <summary>
    /// Abstract base class for <see cref="ISource"/> implementations.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public abstract class AbstractBaseSource : ISource
    {
        #region Constructors

        /// <summary>
        /// Default constructor.
        /// </summary>
        protected AbstractBaseSource()
        {
            extractors = new Dictionary<string, IExtractor>();
        }

        #endregion

        #region ISource implementation

        /// <summary>
        /// Called by the loader to inform source that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        public virtual void BeginExport()
        {
        }

        /// <summary>
        /// Called by the loader to inform source that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        public virtual void EndExport()
        {
        }

        /// <summary>
        /// Return extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property name</param>
        /// <returns>Extractor that should be used for the specified property</returns>
        public virtual IExtractor GetExtractor(string propertyName)
        {
            return extractors.ContainsKey(propertyName)
                       ? extractors[propertyName]
                       : CreateDefaultExtractor(propertyName);
        }

        /// <summary>
        /// Set extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property name</param>
        /// <param name="extractor">
        /// Extractor that should be used for the specified property
        /// </param>
        public virtual void SetExtractor(string propertyName, IExtractor extractor)
        {
            extractors[propertyName] = extractor;
        }

        #endregion

        #region Abstract methods

        /// <summary>
        /// Returns an enumerator that iterates through a collection.
        /// </summary>
        /// <returns>
        /// An <see cref="T:System.Collections.IEnumerator"/> object that can be used to iterate through the collection.
        /// </returns>
        public abstract IEnumerator GetEnumerator();

        /// <summary>
        /// Create default extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property to create an extractor for</param>
        /// <returns>Property extractor instance</returns>
        protected abstract IExtractor CreateDefaultExtractor(string propertyName);

        #endregion

        #region Data members
        
        /// <summary>
        /// A dictionary of registered property extractors for this source.
        /// </summary>
        private IDictionary<string, IExtractor> extractors;

        #endregion
    }
}