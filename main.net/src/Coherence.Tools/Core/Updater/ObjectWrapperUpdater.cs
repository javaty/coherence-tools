using System;
using Spring.Objects;
using Tangosol.IO.Pof;

namespace Seovic.Core.Updater
{
    /// <summary>
    /// Simple imlementation of <see cref="IUpdater"/> that updates single 
    /// property of a target object using Spring.NET ObjectWrapper, thus 
    /// allowing for the automatic conversion of string values to a target 
    /// property type.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    [Serializable]
    public class ObjectWrapperUpdater : IUpdater, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ObjectWrapperUpdater()
        {
        }

        /// <summary>
        /// Construct an <code>ObjectWrapperUpdater</code> instance.
        /// </summary>
        /// <param name="propertyName">
        /// The name of the property to update.
        /// </param>
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

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="other">Object to compare this object with.</param>
        /// <returns>True if objects are equal, false otherwise.</returns>
        public bool Equals(ObjectWrapperUpdater other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_propertyName, m_propertyName);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (ObjectWrapperUpdater)) return false;
            return Equals((ObjectWrapperUpdater) obj);
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
