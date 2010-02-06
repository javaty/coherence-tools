namespace Seovic.Identity
{
    /// <summary>
    /// Simplest possible implementation of <see cref="SequenceGenerator"/>
    /// that stores sequence information locally, in memory.
    /// </summary>
    /// <remarks>
    /// This <see cref="SequenceGenerator"/> implementation is not suitable 
    /// for use when the last assigned sequence number needs to be persisted
    /// across program executions.
    /// <para/>
    /// In general, this implementation should only be used for testing.
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    public class SimpleSequenceGenerator : SequenceGenerator
    {
        /// <summary>
        /// Construct sequence generator.
        /// </summary>
        /// <param name="name">A sequence name.</param>
        /// <param name="blockSize">
        /// The size of the sequence block to allocate at once.
        /// </param>
        public SimpleSequenceGenerator(string name, int blockSize) 
            : base(name, blockSize)
        {
            m_sequence = new Sequence(name);
        }

        #region SequenceGenerator implementation

        /// <summary>
        /// Allocate a new sequence block.
        /// </summary>
        /// <returns>Block of sequential numbers</returns>
        protected override SequenceBlock AllocateSequenceBlock()
        {
            return m_sequence.AllocateBlock(BlockSize);
        }

        #endregion

        private readonly Sequence m_sequence;
    }
}