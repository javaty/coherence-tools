using System;
using System.Reflection;
using Tangosol.IO.Pof;

namespace Seovic.Core.Updater
{
    /// <summary>
    /// Simple imlementation of <see cref="IUpdater"/> that updates single 
    /// property of a target object using reflection.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    [Serializable]
    public class PropertyUpdater : IUpdater, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public PropertyUpdater()
        {
        }

        /// <summary>
        /// Construct <code>PropertyUpdater</code> instance.
        /// </summary>
        /// <param name="propertyName">
        /// The name of the property to update.
        /// </param>
        public PropertyUpdater(string propertyName)
        {
            m_propertyName = propertyName;
        }

        #endregion

        #region IUpdater implementation

        public void Update(object target, object value)
        {
            if (target == null)
            {
                throw new ArgumentException("Updater target cannot be null");
            }

            Type targetType = target.GetType();
            try
            {
                PropertyInfo property = m_property;
                if (property == null || property.DeclaringType != targetType)
                {
                    m_property = property 
                        = targetType.GetProperty(m_propertyName, BINDING_FLAGS);
                }
                if (property.CanWrite)
                {
                    property.SetValue(target, value, null);
                }
                
            }
            catch (NullReferenceException)
            {
                throw new Exception("Writeable property " + m_propertyName +
                                       " does not exist in the class "
                                       + targetType);
            }
        }

        #endregion

        #region IPortableObject

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

        public bool Equals(PropertyUpdater other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_propertyName, m_propertyName);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (PropertyUpdater)) return false;
            return Equals((PropertyUpdater) obj);
        }

        public override int GetHashCode()
        {
            return m_propertyName.GetHashCode();
        }

        public override string ToString()
        {
            return "PropertyUpdater{" +
               "propertyName='" + m_propertyName + '\'' +
               '}';
        }

        #endregion

        #region Data members

        private const BindingFlags BINDING_FLAGS =
            BindingFlags.Public | BindingFlags.Instance | BindingFlags.IgnoreCase;

        /// <summary>
        /// Property name.
        /// </summary>
        private String m_propertyName;

        /// <summary>
        /// Property info.
        /// </summary>
        [NonSerialized]
        private PropertyInfo m_property;

        #endregion
    }
}
