using Seovic.Core;

namespace Seovic.Loader
{
    /// <summary>
    /// Default loader implementation.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public class DefaultLoader : ILoader
    {
        #region Constructors

        /// <summary>
        /// Construct DefaultLoader instance.
        /// </summary>
        /// <param name="source">Source to load items from</param>
        /// <param name="target">Target to load items into</param>
        public DefaultLoader(ISource source, ITarget target)
        {
            this.source = source;
            this.target = target;
        }

        #endregion

        #region ILoader implementation

        /// <summary>
        /// Load items from the ISource into the ITarget.
        /// </summary>
        public void Load()
        {
            source.BeginExport();
            target.BeginImport();
            string[] propertyNames = target.PropertyNames;

            foreach (object sourceItem in source)
            {
                object targetItem = target.CreateTargetInstance(source, sourceItem);
                foreach (string property in propertyNames)
                {
                    IExtractor extractor = source.GetExtractor(property);
                    IUpdater updater     = target.GetUpdater(property);
                    updater.Update(targetItem, extractor.Extract(sourceItem));
                }
                target.ImportItem(targetItem);
            }
            source.EndExport();
            target.EndImport();
        }

        #endregion

        #region Data members

        /// <summary>
        /// Source to load items from.
        /// </summary>
        private ISource source;

        /// <summary>
        /// Target to load items into.
        /// </summary>
        private ITarget target;

        #endregion
    }
}
