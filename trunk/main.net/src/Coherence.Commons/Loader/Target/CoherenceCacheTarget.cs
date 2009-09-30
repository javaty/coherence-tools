using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using Seovic.Coherence.Core;
using Seovic.Coherence.Identity;
using Seovic.Coherence.Identity.Extractor;
using Tangosol.Net;

namespace Seovic.Coherence.Loader.Target
{
    public class CoherenceCacheTarget : AbstractBaseTarget
    {
        #region Constructors

        public CoherenceCacheTarget(INamedCache cache, Type itemType)
            : this(cache, itemType, null, new EntityIdentityExtractor())
        {
        }

        public CoherenceCacheTarget(INamedCache cache, Type itemType,
                                    IIdentityGenerator idGenerator) 
            : this(cache, itemType, idGenerator, null)
        {
        }

        public CoherenceCacheTarget(INamedCache cache, Type itemType,
                                    IIdentityExtractor idExtractor) 
            : this(cache, itemType, null, idExtractor)
        {
        }

        private CoherenceCacheTarget(INamedCache cache, Type itemType,
                                     IIdentityGenerator idGenerator,
                                     IIdentityExtractor idExtractor)
        {
            this.cache       = cache;
            this.itemType    = itemType;
            this.idGenerator = idGenerator;
            this.idExtractor = idExtractor;
        }

        #endregion

        #region ITarget implementation

        public override void BeginImport()
        {
            batch = new Hashtable();
        }

        public override void ImportItem(object item)
        {
            object id = idGenerator != null
                    ? idGenerator.GenerateIdentity()
                    : idExtractor.ExtractIdentity(item);

            batch[id] = item;
            if (batch.Count % batchSize == 0)
            {
                cache.InsertAll(batch);
                batch.Clear();
            }
        }

        public override void EndImport()
        {
            if (batch.Count > 0)
            {
                cache.InsertAll(batch);
            }
        }

        public override object CreateTargetInstance(ISource source, object sourceItem)
        {
            if (itemCtor == null)
            {
                itemCtor = itemType.GetConstructor(Type.EmptyTypes);
            }
            return itemCtor.Invoke(null);
        }

        public override string[] PropertyNames
        {
            get 
            {
                IList<PropertyInfo> properties = GetWriteableProperties(itemType);
                string[] propertyNames = new string[properties.Count];

                int i = 0;
                foreach (PropertyInfo pi in properties)
                {
                    propertyNames[i++] = pi.Name;
                }

                return propertyNames;
            }
        }

        #endregion

        #region Helper methods

        protected override IUpdater CreateDefaultUpdater(string propertyName)
        {
            return Defaults.CreateUpdater(propertyName);
        }

        #endregion

        #region Properties

        public int BatchSize
        {
            set
            {
                batchSize = value;
            }
        }

        #endregion

        #region Constants

        public static readonly int DEFAULT_BATCH_SIZE = 1000;

        #endregion

        #region Data members

        private INamedCache        cache;

        private Type               itemType;

        private IIdentityGenerator idGenerator;

        private IIdentityExtractor idExtractor;

        private IDictionary        batch;

        private int                batchSize = DEFAULT_BATCH_SIZE;

        [NonSerialized]
        private ConstructorInfo    itemCtor;

        #endregion
    }
}