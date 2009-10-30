using System;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Identity.Sequence
{
    /// <summary>
    /// Represents a block of sequential numbers.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    [Serializable]
    public class SequenceBlock : IPortableObject
    {

        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public SequenceBlock()
        {
        }

        /// <summary>
        /// Construct a new sequence block.
        /// </summary>
        /// <param name="first">First number in a sequence</param>
        /// <param name="last">Last number in a sequence</param>
        public SequenceBlock(long first, long last)
        {
            m_next = first;
            m_last = last;
        }

        #endregion

        #region Public API

        /// <summary>
        /// Return <code>true</code> if there are avialable numbers in this
        /// sequence block, <code>false</code> otherwise.
        /// </summary>
        /// <returns>
        /// <code>true</code> if there are avialable numbers in this
        /// sequence block, <code>false</code> otherwise.
        /// </returns>
        public bool HasNext()
        {
            lock (this)
            {
                return m_next <= m_last;
            }
        }

        /// <summary>
        /// Return the next available number in this sequence block.
        /// </summary>
        /// <returns>The next available number in this sequence block</returns>
        public long Next()
        {
            lock (this)
            {
                long old = m_next;
                m_next++;
                return old;
            }
            
        }

        #endregion

        #region IPortableObject implementation 

        public void ReadExternal(IPofReader reader)
        {
            m_next = reader.ReadInt64(0);
            m_last = reader.ReadInt64(1);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteInt64(0, m_next);
            writer.WriteInt64(1, m_last);
        }

        #endregion

        #region Data members

        /// <summary>
        /// The next assignable number within this sequence block.
        /// </summary>
        private long m_next;

        /// <summary>
        /// The last assignable number within this sequence block.
        /// </summary>
        private long m_last;

        #endregion
    }
}
