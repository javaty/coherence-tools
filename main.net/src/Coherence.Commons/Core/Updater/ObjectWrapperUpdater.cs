using System;
using Spring.Objects;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Updater
{
    [Serializable]
    public class ObjectWrapperUpdater : IUpdater, IPortableObject
    {
        #region Constructors

        public ObjectWrapperUpdater()
        {
        }

        public ObjectWrapperUpdater(string propertyName)
        {
            m_propertyName = propertyName;
        }

        #endregion

        #region IUpdater implementation

        public void Update(object target, object value)
        {
            ObjectWrapper wrapper = new ObjectWrapper(target);
            wrapper.SetPropertyValue(m_propertyName, value);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_propertyName = reader.ReadString(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, m_propertyName);
        }

        #endregion

        #region Object methods

        public override bool Equals(object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null || GetType() != typeof(ObjectWrapperUpdater))
            {
                return false;
            }

            ObjectWrapperUpdater that = (ObjectWrapperUpdater) obj;
            return m_propertyName.Equals(that.m_propertyName);
        }

        public override int GetHashCode()
        {
            return m_propertyName.GetHashCode();
        }

        public override string ToString()
        {
            return "ObjectWrapperUpdater{" +
                "propertyName='" + m_propertyName + '\'' +
                '}'; 
        }

        #endregion

        #region Data members

        /// <summary>
        /// Property name.
        /// </summary>
        private string m_propertyName;

        #endregion
    }
}
