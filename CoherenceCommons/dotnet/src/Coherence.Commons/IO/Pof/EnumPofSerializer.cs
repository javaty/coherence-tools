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
using System.IO;

using Tangosol.IO.Pof;

namespace Seovic.Coherence.IO.Pof
{
    /// <summary><see cref="IPofSerializer"/> implementation that supports
    /// serialization and deserialization of enum values.
    /// </summary>
    /// <author>Aleksandar Seovic  2008.10.30</author>
    public class EnumPofSerializer : IPofSerializer
    {
        #region IPofSerializer implementation

        /// <summary>
        /// Serialize enum value into a POF stream. 
        /// </summary>
        /// <remarks>
        /// This method will simply write the name of an enum value as a
        /// string into a POF stream, in order to achieve maximum portability
        /// across platforms.
        /// </remarks>
        /// <param name="writer">
        /// The <b>IPofWriter</b> with which to write the object's state.
        /// </param>
        /// <param name="o">
        /// The enum value to serialize.
        /// </param>
        /// <exception cref="IOException">
        /// If an I/O error occurs.
        /// </exception>
        public void Serialize(IPofWriter writer, object o)
        {
            if (o == null || !o.GetType().IsEnum)
            {
                throw new ArgumentException(
                        "EnumPofSerializer can only be used to serialize enum types.");
            }

            writer.WriteString(0, o.ToString());
            writer.WriteRemainder(null);
        }

        /// <summary>
        /// Deerialize enum value from a POF stream. 
        /// </summary>
        /// <remarks>
        /// This method expects to find the name of an enum value in the
        /// POF stream.
        /// </remarks>
        /// <param name="reader">
        /// The <b>IPofReader</b> with which to read the object's state.
        /// </param>
        /// <returns>
        /// The deserialized enum instance.
        /// </returns>
        /// <exception cref="IOException">
        /// If an I/O error occurs.
        /// </exception>
        public object Deserialize(IPofReader reader)
        {
            IPofContext pofContext = reader.PofContext;
            Type enumType = pofContext.GetType(reader.UserTypeId);
            if (!enumType.IsEnum)
            {
                throw new ArgumentException(
                        "EnumPofSerializer can only be used to deserialize enum types.");
            }

            object enumValue = Enum.Parse(enumType, reader.ReadString(0));
            reader.ReadRemainder();

            return enumValue;
        }

        #endregion
    }
}