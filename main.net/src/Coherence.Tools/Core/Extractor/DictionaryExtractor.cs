using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// Simple imlementation of <see cref="IExtractor"/> that extracts value 
    /// from a dictionary.
    /// </summary>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
    [Serializable]
    public class DictionaryExtractor : IExtractor, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public DictionaryExtractor()
        {
        }

        /// <summary>
        /// Construct an <code>DictionaryExtractor</code> instance.
        /// </summary>
        /// <param name="key">The key to extract value for.</param>
        public DictionaryExtractor(string key)
        {
            m_key = key;
        }

        #endregion

        #region IExtractor implementation

        public object Extract(object target)
        {
             if (target == null)
            {
                return null;
            }
            if (!(target is IDictionary))
            {
            throw new ArgumentException(
                    "Extraction target is not a Dictionary");
            }

            return ((IDictionary) target)[m_key];
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_key = reader.ReadString(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, m_key);
        }

        #endregion

        #region Object methods

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="other">Object to compare this object with.</param>
        /// <returns>True if objects are equal, false otherwise.</returns>
        public bool Equals(DictionaryExtractor other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_key, m_key);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (DictionaryExtractor)) return false;
            return Equals((DictionaryExtractor) obj);
        }

        public override int GetHashCode()
        {
            return (m_key != null ? m_key.GetHashCode() : 0);
        }

        public override string ToString()
        {
            return "DictionaryExtractor{" +
              "key=" + m_key +
              '}';
        }

        #endregion

        #region Data members

        /// <summary>
        /// Dictionary key.
        /// </summary>
        private string m_key;

        #endregion
    }
}
