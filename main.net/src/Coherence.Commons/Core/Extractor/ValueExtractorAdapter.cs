using System;
using Tangosol.IO.Pof;
using Tangosol.Util;

namespace Seovic.Coherence.Core.Extractor
{
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
        /// <param name="mDelegate">Value extractor to delegate to.</param>
        public ValueExtractorAdapter(IValueExtractor mDelegate)
        {
            m_delegate = mDelegate;
        }

        #endregion

        #region IExtractor implementation

        public object Extract(object target)
        {
            return m_delegate.Extract(target);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_delegate = (IValueExtractor) reader.ReadObject(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, m_delegate);
        }

        #endregion

        #region Object methods

        public override bool Equals(object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            ValueExtractorAdapter adapter = (ValueExtractorAdapter)obj;
            return m_delegate.Equals(adapter.m_delegate);
        }

        public override int GetHashCode()
        {
            return m_delegate.GetHashCode();
        }

        public override string ToString()
        {
            return "ValueExtractorAdapter{" +
               "delegate=" + m_delegate +
               '}'; 
        }


        #endregion


        #region Data members

        private IValueExtractor m_delegate;

        #endregion
    }
}
