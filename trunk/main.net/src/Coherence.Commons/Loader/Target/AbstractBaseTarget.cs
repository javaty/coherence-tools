using System;
using System.Collections.Generic;
using System.Reflection;
using Seovic.Coherence.Core;

namespace Seovic.Coherence.Loader.Target
{
    public abstract class AbstractBaseTarget : ITarget
    {
        #region ITarget implementation

        public virtual void BeginImport()
        {
        }

        public virtual void SetUpdater(string propertyName, IUpdater updater)
        {
            updaters[propertyName] = updater;
        }

        public virtual IUpdater GetUpdater(string propertyName)
        {
            IUpdater updater = updaters[propertyName];
            return updater != null
                       ? updater
                       : CreateDefaultUpdater(propertyName);
        }

        public virtual void EndImport()
        {
        }

        public abstract void ImportItem(object item);

        public abstract object CreateTargetInstance(ISource source, object sourceItem);

        public abstract string[] PropertyNames
        {
            get;
        }

        #endregion

        #region Helper methods

        protected IList<PropertyInfo> GetWriteableProperties(Type itemType)
        {
            PropertyInfo[] properties = itemType.GetProperties(BINDING_FLAGS);
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

        protected const BindingFlags BINDING_FLAGS =
            BindingFlags.Public | BindingFlags.Instance | BindingFlags.IgnoreCase;

        private IDictionary<string, IUpdater> updaters = new Dictionary<string, IUpdater>();

        #endregion

    }
}
