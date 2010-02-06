using System;
using Tangosol.IO;
using Tangosol.Util;

namespace Seovic.Coherence.Util
{
    /// <summary>
    /// Miscellaneous serialization utilities.
    /// </summary>
    public static class SerializationHelper
    {
        /// <summary>
        /// Serialize object into its binary form.
        /// </summary>
        /// <param name="obj">Object to serialize.</param>
        /// <param name="serializer">Serializer to use.</param>
        /// <returns>
        /// Serialized binary representation of the specified object.
        /// </returns>
        public static Binary ToBinary(Object obj, ISerializer serializer)
        {
            BinaryMemoryStream buf = new BinaryMemoryStream();
            serializer.Serialize(new DataWriter(buf), obj);
            return buf.ToBinary();
        }

        /// <summary>
        /// Deserialized object from its binary form.
        /// </summary>
        /// <param name="bin">Binary representation of an object.</param>
        /// <param name="serializer">Serializer to use.</param>
        /// <returns>Deserialized object.</returns>
        public static Object FromBinary(Binary bin, ISerializer serializer)
        {
            return serializer.Deserialize(new DataReader(bin.GetStream()));
        }
    }
}