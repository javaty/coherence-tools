using Seovic.Config;
using Seovic.Identity;
using Tangosol.IO.Pof;
using Tangosol.Net;
using Tangosol.Net.Cache;
using Tangosol.Util.Processor;

namespace Seovic.Coherence.Identity
{
    /// <summary>
    /// Implementation of <see cref="SequenceGenerator"/> that stores
    /// sequences in a Coherence cache.
    /// </summary>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    public class CoherenceSequenceGenerator : SequenceGenerator
    {
        #region Constructors

        /// <summary>
        /// Construct sequence generator.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        /// <param name="blockSize">
        /// The size of the sequence block to allocate at once.
        /// </param>
        public CoherenceSequenceGenerator(string name, int blockSize) 
            : base(name, blockSize)
        {
        }

        #endregion

        #region SequenceGenerator implementation

        /// <summary>
        /// Allocate a new sequence block.
        /// </summary>
        /// <returns>Block of sequential numbers</returns>
        protected override SequenceBlock AllocateSequenceBlock()
        {
            return (SequenceBlock)
                   s_sequenceCache.Invoke(SequenceName, new SequenceBlockAllocator(BlockSize));
        }

        #endregion

        #region Inner class: SequenceBlockAllocator

        /// <summary>
        /// An entry processor that allocates a block of sequential number from a named
        /// sequence.
        /// </summary>
        /// <remarks>
        /// If the sequence entry for the given name does not already exist in the cache,
        /// it will be created automatically.
        /// </remarks>
        /// <author>Aleksandar Seovic  2009.05.27</author>
        public class SequenceBlockAllocator : AbstractProcessor, IPortableObject
        {
            #region Constructors

            /// <summary>
            /// Deserialization constructor (for internal use only).
            /// </summary>
            public SequenceBlockAllocator()
            {
            }

            /// <summary>
            /// Construct new sequence block allocator.
            /// </summary>
            /// <param name="blockSize">
            /// The size of the sequence block to allocate.
            /// </param>
            public SequenceBlockAllocator(int blockSize)
            {
                m_blockSize = blockSize;
            }

            #endregion

            #region IEntryProcessor implementation

            public override object Process(IInvocableCacheEntry entry)
            {
                Sequence sequence = (Sequence)entry.Value;
                if (sequence == null)
                {
                    sequence = new Sequence((string) entry.Key);
                }

                SequenceBlock block = sequence.AllocateBlock(m_blockSize);
                entry.Value = sequence;

                return block;
            }

            #endregion

            #region IPortableObject implementation

            public void ReadExternal(IPofReader reader)
            {
                m_blockSize = reader.ReadInt32(0);
            }

            public void WriteExternal(IPofWriter writer)
            {
                writer.WriteInt32(0, m_blockSize);
            }

            #endregion

            #region Object methods

            /// <summary>
            /// Test objects for equality.
            /// </summary>
            /// <param name="other">Object to compare this object with.</param>
            /// <returns>True if objects are equal, false otherwise.</returns>
            public bool Equals(SequenceBlockAllocator other)
            {
                if (ReferenceEquals(null, other)) return false;
                if (ReferenceEquals(this, other)) return true;
                return other.m_blockSize == m_blockSize;
            }

            public override bool Equals(object obj)
            {
                if (ReferenceEquals(null, obj)) return false;
                if (ReferenceEquals(this, obj)) return true;
                if (obj.GetType() != typeof(SequenceBlockAllocator)) return false;
                return Equals((SequenceBlockAllocator)obj);
            }

            public override int GetHashCode()
            {
                return m_blockSize;
            }

            public override string ToString()
            {
                return "SequenceBlockAllocator{" +
                       "blockSize=" + m_blockSize +
                       '}';
            }

            #endregion

            #region Data members

            /// <summary>
            /// The size of the sequence block to allocate.
            /// </summary>
            private int m_blockSize;

            #endregion
        }

        #endregion

        #region Data members

        /// <summary>
        /// The name of the sequences cache.
        /// </summary>
        public static readonly string CACHE_NAME =
            Configuration.GetSequenceCacheName();

        /// <summary>
        /// Sequences cache.
        /// </summary>
        private static readonly INamedCache s_sequenceCache =
            CacheFactory.GetCache(CACHE_NAME);

        #endregion
    }
}