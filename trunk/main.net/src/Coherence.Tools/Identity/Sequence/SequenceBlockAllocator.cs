
using Tangosol.IO.Pof;
using Tangosol.Net.Cache;
using Tangosol.Util.Processor;

namespace Seovic.Coherence.Identity.Sequence
{
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
        /// <param name="blockSize">The size of the sequence block to allocate</param>
        public SequenceBlockAllocator(int blockSize)
        {
            m_blockSize = blockSize;
        }

        #endregion

        #region IEntryProcessor implementation

        public override object Process(IInvocableCacheEntry entry)
        {
            Sequence sequence = (Sequence) entry.Value;
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
            if (obj.GetType() != typeof (SequenceBlockAllocator)) return false;
            return Equals((SequenceBlockAllocator) obj);
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
}
