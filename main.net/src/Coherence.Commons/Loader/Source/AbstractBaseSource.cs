using System.Collections;
using System.Collections.Generic;
using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader.Source
{
    public abstract class AbstractBaseSource : ISource
    {
        #region Constructors

        protected AbstractBaseSource()
        {
            extractors = new Dictionary<string, IExtractor>();
        }

        #endregion

        #region ISource implementation

        public virtual void BeginExport()
        {
        }

        public virtual void EndExport()
        {
        }

        public virtual IExtractor GetExtractor(string propertyName)
        {
            IExtractor extractor = extractors[propertyName];
            return extractor != null
                       ? extractor
                       : CreateDefaultExtractor(propertyName);
        }

        public virtual void SetExtractor(string propertyName, IExtractor extractor)
        {
            extractors[propertyName] = extractor;
        }

        #endregion

        #region Abstract methods

        public abstract IEnumerator GetEnumerator();

        protected abstract IExtractor CreateDefaultExtractor(string propertyName);

        #endregion

        #region Data members

        private IDictionary<string, IExtractor> extractors;

        #endregion
    }
}