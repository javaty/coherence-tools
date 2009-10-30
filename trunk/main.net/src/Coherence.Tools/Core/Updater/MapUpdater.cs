using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Updater
{
    [Serializable]
    public class MapUpdater : IUpdater, IPortableObject
    {
        #region Constructors

        public MapUpdater()
        {
        }

        public MapUpdater(string mKey)
        {
            m_key = mKey;
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

        public bool Equals(MapUpdater other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_key, m_key);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (MapUpdater)) return false;
            return Equals((MapUpdater) obj);
        }

        public override int GetHashCode()
        {
            return m_key.GetHashCode();
        }

        public override string ToString()
        {
            return "MapUpdater{" +
              "key=" + m_key +
              '}';
        }

        #endregion

        #region Data members

        private string m_key;

        #endregion
    }
}
