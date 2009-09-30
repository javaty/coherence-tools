using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Seovic.Coherence.Core;
using Tangosol.Net;

namespace Seovic.Coherence.Loader.Source
{
    public class CoherenceCacheSource : AbstractBaseSource
    {
        #region Constructors

        public CoherenceCacheSource(string cacheName)
        {
            cache = CacheFactory.GetCache(cacheName);
        }

        #endregion

        #region IEnumerable implementation

        /// <summary>
        /// Returns an enumerator that iterates through a collection.
        /// </summary>
        /// <returns>
        /// An <see cref="T:System.Collections.IEnumerator"/> object that can be used to iterate through the collection.
        /// </returns>
        /// <filterpriority>2</filterpriority>
        public override IEnumerator GetEnumerator()
        {
            return cache.Values.GetEnumerator();
        }

        #endregion

        #region AbstractBaseSource implementation

        protected override IExtractor CreateDefaultExtractor(string propertyName)
        {
            return Defaults.CreateExtractor(propertyName);
        }

        #endregion

        #region Data members

        private INamedCache cache;

        #endregion
    }
}