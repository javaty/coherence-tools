using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Core.Updater
{
    /// <summary>
    /// Simple imlementation of <see cref="IUpdater"/> that updates 
    /// single dictionary entry.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    [Serializable]
    public class DictionaryUpdater : IUpdater, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public DictionaryUpdater()
        {
        }

        /// <summary>
        /// Construct a <code>DictionaryUpdater</code> instance.
        /// </summary>
        /// <param name="key">The key of the entry to update.</param>
        public DictionaryUpdater(string key)
        {
            m_key = key;
        }

        #endregion

        #region IUpdater implementation

        public void Update(object target, object value)
        {
            if (target == null)
            {
                throw new ArgumentException("Updater target cannot be null");
            }
            if (!(target is IDictionary))
            {
                throw new ArgumentException("Updater target is not a Dictionary");
            }

            ((IDictionary) target)[m_key] = value;
        }

        #endregion

        #region IPortableObject

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
        public bool Equals(DictionaryUpdater other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_key, m_key);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (DictionaryUpdater)) return false;
            return Equals((DictionaryUpdater) obj);
        }

        public override int GetHashCode()
        {
            return m_key.GetHashCode();
        }

        public override string ToString()
        {
            return "DictionaryUpdater{" +
              "key=" + m_key +
              '}';
        }

        #endregion

        #region Data members

        private string m_key;

        #endregion
    }
}
