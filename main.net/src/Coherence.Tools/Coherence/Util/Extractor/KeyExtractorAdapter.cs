using System;

using Seovic.Core;

using Tangosol.Util;
using Tangosol.Util.Extractor;

namespace Seovic.Coherence.Util.Extractor
{
    /// <summary>
    /// Adapter that allows any class that implements <c>Tangosol.Util.IValueExtractor</c>
    /// to be used where <see cref="IExtractor"/> instance is expected.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    [Serializable]
    public class KeyExtractorAdapter : KeyExtractor, IExtractor
    {
        #region Constructor

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public KeyExtractorAdapter()
        {
        }

        /// <summary>
        /// Construct a <code>ValueExtractorAdapter</code> instance.
        /// </summary>
        /// <param name="extractor">Value extractor to delegate to.</param>
        public KeyExtractorAdapter(IValueExtractor extractor)
            : base(extractor)
        {
        }

        #endregion
    }
}