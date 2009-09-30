using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader
{
    public interface ITarget
    {
        void BeginImport();

        void ImportItem(object item);

        void EndImport();

        object CreateTargetInstance(ISource source, object sourceItem);

        IUpdater GetUpdater(string propertyName);

        void SetUpdater(string propertyName, IUpdater updater);

        string[] PropertyNames
        { 
            get;
        }
    }
}
