/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

using System;
using System.Reflection;

using Tangosol.IO.Pof;
using Tangosol.Util;
using Tangosol.Util.Extractor;

namespace Seovic.Coherence.Util.Extractor
{
    /// <summary>
    /// A simple implementation of a ValueExtractor that uses 
    /// reflection to retrieve property value.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    public class PropertyExtractor : AbstractExtractor, IValueExtractor, IPortableObject
    {
        #region Data members

        private String m_propertyName;

        [NonSerialized]
        private PropertyInfo m_property;

        #endregion

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

        /// <summary>
        /// Construct a <c>PropertyExtractor</c> instance.
        /// </summary>
        /// <param name="propertyName">
        /// The name of the property to extract.
        /// </param>
        /// <param name="target">
        /// Extraction target (KEY or VALUE).
        /// </param>
        public PropertyExtractor(String propertyName, int target)
        {
            m_propertyName = propertyName;
            m_target       = target;
        }

        #endregion

        #region Implementation of IValueExtractor

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
        public override Object Extract(Object target)
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
                    m_property = property = targetType.GetProperty(m_propertyName, 
                        BindingFlags.Public | BindingFlags.Instance | BindingFlags.IgnoreCase);
                }
                return property.GetValue(target, null);
            }
            catch (NullReferenceException)
            {
                throw new Exception("Property " + m_propertyName +
                                           " does not exist" +
                                           " in the class " + targetType);
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
            m_target       = reader.ReadInt32( 1);
        }

        /// <summary>
        /// Serialize this object into a POF stream.
        /// </summary>
        /// <param name="writer">POF writer to use.</param>
        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, m_propertyName);
            writer.WriteInt32( 1, m_target);
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
            return Equals(obj.m_propertyName, m_propertyName) && obj.m_target == m_target;
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
            if (obj.GetType() != typeof (PropertyExtractor)) return false;
            return Equals((PropertyExtractor) obj);
        }

        /// <summary>
        /// Return a hash code for this object.
        /// </summary>
        /// <returns>A hash code for this object.</returns>
        public override int GetHashCode()
        {
            unchecked
            {
                return (m_propertyName.GetHashCode()*397) ^ m_target;
            }
        }

        /// <summary>
        /// Return string representation of this object.
        /// </summary>
        /// <returns>A string representation of this object.</returns>
        public override string ToString()
        {
            return "PropertyExtractor{" +
                   "propertyName='" + m_propertyName + 
                   "', target=" + (m_target == KEY ? "KEY" : "VALUE") +
                   '}';
        }

        #endregion
    }
}