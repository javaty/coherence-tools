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

        public override int GetHashCode()
        {
            return m_key.GetHashCode();
        }

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

            MapExtractor extractor = (MapExtractor) obj;
            return m_key.Equals(extractor.m_key);
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
