using System;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Identity.Sequence
{
    /// <summary>
    /// Represents a named sequence.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    [Serializable]
    public class Sequence : IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public Sequence()
        {
        }

        /// <summary>
        /// Sequence constructor.
        /// </summary>
        /// <param name="name">Sequence name</param>
        public Sequence(string name)
        {
            m_name = name;
        }

        #endregion

        #region Public API

        /// <summary>
        /// Allocate a block of sequence numbers, starting from the last allocated
        /// sequence value.
        /// </summary>
        /// <param name="blockSize">The number of sequences to allocate</param>
        /// <returns>Allocated block of sequential numbers</returns>
        public SequenceBlock AllocateBlock(int blockSize)
        {
            long last = m_last;
            SequenceBlock block = new SequenceBlock(last + 1, last + blockSize);
            m_last = last + blockSize;

            return block;
        }

        #endregion

        #region Properties

        /// <summary>
        /// Return the sequence name.
        /// </summary>
        public string Name
        {
            get
            {
                return m_name;
            }
        }

        /// <summary>
        /// Return the last allocated sequence number.
        /// </summary>
        public long Peek
        {
            get
            {
                return m_last;
            }
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_name = reader.ReadString(0);
            m_last = reader.ReadInt64(1);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, m_name);
            writer.WriteInt64(1, m_last);
        }

        #endregion

        #region Data members

        /// <summary>
        /// Sequence name.
        /// </summary>
        private string m_name;

        /// <summary>
        /// The last allocated number from this sequence.
        /// </summary>
        private long m_last;

        #endregion

    }
}
