using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using Seovic.Coherence.Core;
using Seovic.Coherence.Identity;
using Seovic.Coherence.Identity.Extractor;
using Seovic.Coherence.Util;
using Tangosol.Net;

namespace Seovic.Coherence.Loader.Target
{
    /// <summary>
    /// A <see cref="ITarget"/> implementation that load objects into Coherence cache.
    /// </summary>
    public class CoherenceCacheTarget : AbstractBaseTarget
    {
        #region Constructors

        /// <summary>
        /// Construct CoherenceCacheTarget instance.
        /// </summary>
        /// <remarks>
        /// This constructor assumes that target class implements <see cref="Core.IEntity{TId}"/>
        /// interface and will use <see cref="EntityIdentityExtractor"/> to determine.
        /// cache key.
        /// </remarks>
        /// <param name="cache">cache to load objects into</param>
        /// <param name="itemType">target item type</param>
        public CoherenceCacheTarget(INamedCache cache, Type itemType)
            : this(cache, itemType, null, new EntityIdentityExtractor())
        {
        }

        /// <summary>
        /// Construct CoherenceCacheTarget instance.
        /// </summary>
        /// <param name="cache">Cache to load objects into</param>
        /// <param name="itemType">Target item type</param>
        /// <param name="idGenerator">Identity generator to use to determine key</param>
        public CoherenceCacheTarget(INamedCache cache, Type itemType,
                                    IIdentityGenerator idGenerator) 
            : this(cache, itemType, idGenerator, null)
        {
        }

        /// <summary>
        /// Construct CoherenceCacheTarget instance.
        /// </summary>
        /// <param name="cache">Cache to load objects into</param>
        /// <param name="itemType">Target item type</param>
        /// <param name="idExtractor">Identity extractor to use to determine key</param>
        public CoherenceCacheTarget(INamedCache cache, Type itemType,
                                    IIdentityExtractor idExtractor) 
            : this(cache, itemType, null, idExtractor)
        {
        }

        /// <summary>
        /// Construct CoherenceCacheTarget instance.
        /// </summary>
        /// <param name="cache">Cache to load objects into</param>
        /// <param name="itemType">Target item type</param>
        /// <param name="idGenerator">Identity generator to use to determine key</param>
        /// <param name="idExtractor">Identity extractor to use to determine key</param>
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

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        public override void BeginImport()
        {
            batch = new Hashtable();
        }
        /// <summary>
        /// Import a single item.
        /// </summary>
        /// <param name="item">item to import</param>
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

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// * This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        public override void EndImport()
        {
            if (batch.Count > 0)
            {
                cache.InsertAll(batch);
            }
        }

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
        public override object CreateTargetInstance(ISource source, object sourceItem)
        {
            if (itemCtor == null)
            {
                itemCtor = itemType.GetConstructor(Type.EmptyTypes);
            }
            return itemCtor.Invoke(null);
        }

        /// <summary>
        /// Return target property names.
        /// </summary>
        /// <remarks>
        /// Because it is ultimately the target that determines what needs to be
        /// loaded, this method provides target implementations a way to control
        /// which properties from the source they care about.
        /// </remarks>
        public override string[] PropertyNames
        {
            get 
            {
                IList<PropertyInfo> properties = GetWriteableProperties(itemType);
                string[] propertyNames = new string[properties.Count];

                int i = 0;
                foreach (PropertyInfo pi in properties)
                {
                    propertyNames[i++] = pi.Name.Decapitalize();
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