using System;
using System.Reflection;
using Seovic.Config;
using Tangosol.IO.Pof;

namespace Seovic.Identity
{
    /// <summary>
    /// An <see cref="IIdentityGenerator" /> implementation that generates 
    /// sequential long identifiers.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.05.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public abstract class SequenceGenerator : IIdentityGenerator<long>
    {
        #region Constructors and factory methods

        /// <summary>
        /// Construct sequence generator.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        /// <param name="blockSize">
        /// The size of the sequence block to allocate at once.
        /// </param>
        protected SequenceGenerator(string name, int blockSize)
        {
            m_name      = name;
            m_blockSize = blockSize;
        }

        /// <summary>
        /// Sequence generator factory method.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        public static SequenceGenerator Create(string name)
        {
            return Create(name, DEFAULT_BLOCK_SIZE);
        }

        /// <summary>
        /// Sequence generator factory method.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        /// <param name="blockSize">
        /// The size of the sequence block to allocate at once.
        /// </param>
        public static SequenceGenerator Create(string name, int blockSize)
        {
            Type type = Configuration.GetSequenceGeneratorType();
            ConstructorInfo ctor = type.GetConstructor(
                new Type[] {typeof(string), typeof(int)});
            return (SequenceGenerator) ctor.Invoke(new object[] {name, blockSize});
        }

        #endregion

        #region Properties

        /// <summary>
        /// Sequence name.
        /// </summary>
        public string SequenceName
        {
            get { return m_name; }
        }

        /// <summary>
        /// Sequence block size.
        /// </summary>
        public int BlockSize
        {
            get { return m_blockSize; }
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

        #region Abstract methods

        /// <summary>
        /// Allocate a new sequence block.
        /// </summary>
        /// <returns>Block of sequential numbers</returns>
        protected abstract SequenceBlock AllocateSequenceBlock();

        #endregion

        #region Inner class: Sequence

        /// <summary>
        /// Named sequence.
        /// </summary>
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
            public long Last
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

        #endregion

        #region Inner class: SequenceBlock

        /// <summary>
        /// Represents a block of sequential numbers.
        /// </summary>
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
                    return m_next++;
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

        #endregion

        #region Data members

        /// <summary>
        /// Default sequence block size.
        /// </summary>
        public static readonly int DEFAULT_BLOCK_SIZE = 20;

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
