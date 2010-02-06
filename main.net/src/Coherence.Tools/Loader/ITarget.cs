using Seovic.Core;

namespace Seovic.Loader
{
    /// <summary>
    /// A target that the items can be loaded into.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.06.15</author>
    public interface ITarget
    {
        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        void BeginImport();

        /// <summary>
        /// Import a single item.
        /// </summary>
        /// <param name="item">item to import</param>
        void ImportItem(object item);

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// * This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        void EndImport();

        /// <summary>
        /// Create an instance of a target object.
        /// </summary>
        /// <param name="source">
        /// Source object is loaded from
        /// </param>
        /// <param name="sourceItem">
        /// Source object, in a format determined by its source
        /// </param>
        /// <returns>
        /// A target object instance.
        /// </returns>
        object CreateTargetInstance(ISource source, object sourceItem);

        /// <summary>
        /// Return updater for the specified property.
        /// </summary>
        /// <param name="propertyName">property name</param>
        /// <returns>Updater that should be used for the specified property</returns>
        IUpdater GetUpdater(string propertyName);

        /// <summary>
        /// Set updater for the specified property.
        /// </summary>
        /// <param name="propertyName">Property name</param>
        /// <param name="updater">
        /// Updater that should be used for the specified property
        /// </param>
        void SetUpdater(string propertyName, IUpdater updater);

        /// <summary>
        /// Return target property names.
        /// </summary>
        /// <remarks>
        /// Because it is ultimately the target that determines what needs to be
        /// loaded, this method provides target implementations a way to control
        /// which properties from the source they care about.
        /// </remarks>
        string[] PropertyNames
        { 
            get;
        }
    }
}
