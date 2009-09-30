using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader
{
    public class DefaultLoader : ILoader
    {
        #region Constructors

        public DefaultLoader(ISource source, ITarget target)
        {
            this.source = source;
            this.target = target;
        }

        #endregion

        #region ILoader implementation

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

        private ISource source;

        private ITarget target;

        #endregion
    }
}
