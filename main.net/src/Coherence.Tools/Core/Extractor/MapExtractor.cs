using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Extractor
{
    [Serializable]
    public class MapExtractor : IExtractor, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public MapExtractor()
        {
        }

        /// <summary>
        /// Construct an <code>MapExtractor</code> instance.
        /// </summary>
        /// <param name="key">The key to extract value for.</param>
        public MapExtractor(string key)
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

        public bool Equals(MapExtractor other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_key, m_key);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (MapExtractor)) return false;
            return Equals((MapExtractor) obj);
        }

        public override int GetHashCode()
        {
            return (m_key != null ? m_key.GetHashCode() : 0);
        }

        public override string ToString()
        {
            return "MapExtractor{" +
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
