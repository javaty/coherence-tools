using System;

using Seovic.Core;

using Tangosol.IO.Pof;
using Tangosol.Util;

namespace Seovic.Coherence.Util.Filter
{
    /// <summary>
    /// Adapter that allows any class that implements <c>Tangosol.Util.IFilter</c>
    /// to be used where <see cref="ICondition"/> instance is expected.
    /// </summary>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
    [Serializable]
    public class FilterAdapter : ICondition, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public FilterAdapter()
        {
        }

        /// <summary>
        /// Construct a <c>FilterAdapter</c> instance.
        /// </summary>
        /// <param name="filter">Filter to delegate to.</param>
        public FilterAdapter(IFilter filter)
        {
            m_delegate = filter;
        }

        #endregion

        #region ICondition implementation

        public bool Evaluate(object o)
        {
            return m_delegate.Evaluate(o);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_delegate = (IFilter) reader.ReadObject(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, m_delegate);
        }

        #endregion

        #region Object methods

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="other">Object to compare this object with.</param>
        /// <returns>True if objects are equal, false otherwise.</returns>
        public bool Equals(FilterAdapter other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_delegate, m_delegate);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (FilterAdapter)) return false;
            return Equals((FilterAdapter) obj);
        }

        public override int GetHashCode()
        {
            return m_delegate.GetHashCode();
        }

        public override string ToString()
        {
            return "FilterAdapter{" +
                   "delegate=" + m_delegate +
                   '}';
        }

        #endregion

        #region Data members

        private IFilter m_delegate;

        #endregion
    }
}