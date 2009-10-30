using System.Collections;
using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader
{
    /// <summary>
    /// An iterable source that the items can be loaded from.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public interface ISource : IEnumerable
    {
        /// <summary>
        /// Called by the loader to inform source that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        void BeginExport();

        /// <summary>
        /// Called by the loader to inform source that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        void EndExport();

        /// <summary>
        /// Return extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property name</param>
        /// <returns>Extractor that should be used for the specified property</returns>
        IExtractor GetExtractor(string propertyName);

        /// <summary>
        /// Set extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property name</param>
        /// <param name="extractor">
        /// Extractor that should be used for the specified property
        /// </param>
        void SetExtractor(string propertyName, IExtractor extractor);
    }
}