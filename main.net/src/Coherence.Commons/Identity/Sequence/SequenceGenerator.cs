using System;
using Tangosol.Net;

namespace Seovic.Coherence.Identity.Sequence
{
    /// <summary>
    /// An <see cref="IIdentityGenerator" /> implementation that generates 
    /// sequential long identifiers.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public class SequenceGenerator : IIdentityGenerator<long>
    {
        #region Constructors

        /// <summary>
        /// Construct sequence generator.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        public SequenceGenerator(string name)
            : this(name, DEFAULT_BLOCK_SIZE)
        {
        }

        /// <summary>
        /// Construct sequence generator.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        /// <param name="blockSize">The size of the sequence block to allocate at once</param>
        public SequenceGenerator(string name, int blockSize)
        {
            m_name = name;
            m_blockSize = blockSize;
        }

        #endregion

        #region IIdentityGenerator implementation

        /// <summary>
        /// Generates new identity instance.
        /// </summary>
        /// <returns>New identity instance</returns>
        public long GenerateIdentity()
        {
            lock (this)
            {
                SequenceBlock allocatedSequences = m_allocatedSequences;
                if (allocatedSequences == null || !allocatedSequences.HasNext())
                {
                    m_allocatedSequences = allocatedSequences = AllocateSequenceBlock();
                }
                return allocatedSequences.Next();
            }
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Allocate a new sequence block.
        /// </summary>
        /// <returns>Block of sequential numbers</returns>
        protected SequenceBlock AllocateSequenceBlock()
        {
            return (SequenceBlock)
                    s_sequenceCache.Invoke(m_name, new SequenceBlockAllocator(m_blockSize));
        }


        #endregion

        #region Data members

        public static readonly int DEFAULT_BLOCK_SIZE = 20;

        /// <summary>
        /// The name of the sequences cache.
        /// </summary>
        public static readonly string CACHE_NAME = "coh-tools-sequences";

        /// <summary>
        /// The name of the sequences cache.
        /// </summary>
        private static readonly INamedCache s_sequenceCache = CacheFactory.GetCache(CACHE_NAME);

        /// <summary>
        /// Sequence name.
        /// </summary>
        private readonly String m_name;

        /// <summary>
        /// Sequence block size.
        /// </summary>
        private readonly int m_blockSize;

        /// <summary>
        /// Currently allocated block of sequences.
        /// </summary>
        private SequenceBlock m_allocatedSequences;

        #endregion
    }
}
