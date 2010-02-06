using System;

using Seovic.Core;

using Tangosol.IO.Pof;
using Tangosol.Util;

namespace Seovic.Coherence.Util.Extractor
{
    /// <summary>
    /// Adapter that allows any class that implements <c>Tangosol.Util.IValueExtractor</c>
    /// to be used where <see cref="IExtractor"/> instance is expected.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    [Serializable]
    public class ValueExtractorAdapter : IExtractor, IPortableObject
    {
        #region Constructor

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ValueExtractorAdapter()
        {
        }

        /// <summary>
        /// Construct a <code>ValueExtractorAdapter</code> instance.
        /// </summary>
        /// <param name="extractor">Value extractor to delegate to.</param>
        public ValueExtractorAdapter(IValueExtractor extractor)
        {
            m_extractor = extractor;
        }

        #endregion

        #region IExtractor implementation

        public object Extract(object target)
        {
            return m_extractor.Extract(target);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_extractor = (IValueExtractor) reader.ReadObject(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, m_extractor);
        }

        #endregion

        #region Object methods

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="other">Object to compare this object with.</param>
        /// <returns>True if objects are equal, false otherwise.</returns>
        public bool Equals(ValueExtractorAdapter other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_extractor, m_extractor);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (ValueExtractorAdapter)) return false;
            return Equals((ValueExtractorAdapter) obj);
        }

        public override int GetHashCode()
        {
            return m_extractor.GetHashCode();
        }

        public override string ToString()
        {
            return "ValueExtractorAdapter{" +
                   "delegate=" + m_extractor +
                   '}'; 
        }


        #endregion

        #region Data members

        private IValueExtractor m_extractor;

        #endregion
    }
}