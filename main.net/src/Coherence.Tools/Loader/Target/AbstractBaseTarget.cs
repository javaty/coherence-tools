using System;
using System.Collections.Generic;
using System.Reflection;
using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader.Target
{
    /// <summary>
    /// Abstract base class for <see cref="ITarget"/> implementations.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public abstract class AbstractBaseTarget : ITarget
    {
        #region ITarget implementation

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        public virtual void BeginImport()
        {
        }

        /// <summary>
        /// Set updater for the specified property.
        /// </summary>
        /// <param name="propertyName">Property name</param>
        /// <param name="updater">
        /// Updater that should be used for the specified property
        /// </param>
        public virtual void SetUpdater(string propertyName, IUpdater updater)
        {
            updaters[propertyName] = updater;
        }

        /// <summary>
        /// Return updater for the specified property.
        /// </summary>
        /// <param name="propertyName">property name</param>
        /// <returns>Updater that should be used for the specified property</returns>
        public virtual IUpdater GetUpdater(string propertyName)
        {
            return updaters.ContainsKey(propertyName)
                       ? updaters[propertyName]
                       : CreateDefaultUpdater(propertyName);
        }

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// * This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        public virtual void EndImport()
        {
        }

        /// <summary>
        /// Import a single item.
        /// </summary>
        /// <param name="item">item to import</param>
        public abstract void ImportItem(object item);

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
        public abstract object CreateTargetInstance(ISource source, object sourceItem);

        /// <summary>
        /// Return target property names.
        /// </summary>
        /// <remarks>
        /// Because it is ultimately the target that determines what needs to be
        /// loaded, this method provides target implementations a way to control
        /// which properties from the source they care about.
        /// </remarks>
        public abstract string[] PropertyNames
        {
            get;
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Returns a list of all writeable properties of a class.
        /// </summary>
        /// <param name="itemType">
        /// Type to obtain a list of writeable properties for.
        /// </param>
        /// <returns>
        /// A list of writeable properties of the specified type.
        /// </returns>
        protected IList<PropertyInfo> GetWriteableProperties(Type itemType)
        {
            PropertyInfo[]      properties          = itemType.GetProperties(BINDING_FLAGS);
            IList<PropertyInfo> writeableProperties = new List<PropertyInfo>();
            foreach (PropertyInfo property in properties)
            {
                if (property.CanWrite)
                {
                    writeableProperties.Add(property);
                }
            }
            return writeableProperties;
        }

        protected abstract IUpdater CreateDefaultUpdater(string propertyName);

        #endregion

        #region Data members

        /// <summary>
        /// Binding flags
        /// </summary>
        protected const BindingFlags BINDING_FLAGS =
            BindingFlags.Public | BindingFlags.Instance | BindingFlags.IgnoreCase;

        /// <summary>
        /// A dictionary of registered property updaters for this target.
        /// </summary>
        private IDictionary<string, IUpdater> updaters = new Dictionary<string, IUpdater>();

        #endregion

    }
}
