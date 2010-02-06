using System;
using System.Reflection;
using Tangosol.IO.Pof;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// A simple implementation of a <see cref="IExtractor"/> that uses 
    /// reflection to retrieve property value.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    [Serializable]
    public class PropertyExtractor : IExtractor, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public PropertyExtractor()
        {
        }

        /// <summary>
        /// Construct a <c>PropertyExtractor</c> instance.
        /// </summary>
        /// <param name="propertyName">
        /// The name of the property to extract.
        /// </param>
        public PropertyExtractor(String propertyName)
        {
            m_propertyName = propertyName;
        }

        #endregion

        #region IExtractor implementation

        /// <summary>
        /// Extracts value of the property represented by this extractor
        /// from a specified object.
        /// </summary>
        /// <param name="target">
        /// Object to extract property value from.
        /// </param>
        /// <returns>
        /// Extracted property value.
        /// </returns>
        public object Extract(object target)
        {
            if (target == null)
            {
                return null;
            }

            Type targetType = target.GetType();
            try
            {
                PropertyInfo property = m_property;
                if (property == null
                    || property.DeclaringType != targetType)
                {
                    m_property = property
                        = targetType.GetProperty(m_propertyName, BINDING_FLAGS);
                }
                return property.GetValue(target, null);
            }
            catch (NullReferenceException)
            {
                throw new Exception("Property [" + m_propertyName +
                                    "] does not exist in the class [" + targetType + ']');
            }
        }

        #endregion

        #region Implementation of IPortableObject

        /// <summary>
        /// Deserialize this object from a POF stream.
        /// </summary>
        /// <param name="reader">POF reader to use.</param>
        public void ReadExternal(IPofReader reader)
        {
            m_propertyName = reader.ReadString(0);
        }

        /// <summary>
        /// Serialize this object into a POF stream.
        /// </summary>
        /// <param name="writer">POF writer to use.</param>
        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, m_propertyName);
        }

        #endregion

        #region Object methods

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="obj">
        /// Object to compare this object with.
        /// </param>
        /// <returns>
        /// <c>true</c> if the specified object is equal to this object,
        /// <c>false</c> otherwise.
        /// </returns>
        public bool Equals(PropertyExtractor obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return Equals(obj.m_propertyName, m_propertyName);
        }

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="obj">
        /// Object to compare this object with.
        /// </param>
        /// <returns>
        /// <c>true</c> if the specified object is equal to this object,
        /// <c>false</c> otherwise.
        /// </returns>
        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(PropertyExtractor)) return false;
            return Equals((PropertyExtractor)obj);
        }

        /// <summary>
        /// Return a hash code for this object.
        /// </summary>
        /// <returns>A hash code for this object.</returns>
        public override int GetHashCode()
        {
            return m_propertyName.GetHashCode();
        }

        /// <summary>
        /// Return string representation of this object.
        /// </summary>
        /// <returns>A string representation of this object.</returns>
        public override string ToString()
        {
            return "PropertyExtractor{" +
                   "propertyName='" + m_propertyName +
                   '}';
        }

        #endregion

        #region Data members

        private const BindingFlags BINDING_FLAGS =
            BindingFlags.Public | BindingFlags.Instance | BindingFlags.IgnoreCase;

        private String m_propertyName;

        [NonSerialized]
        private PropertyInfo m_property;

        #endregion
    }
}
