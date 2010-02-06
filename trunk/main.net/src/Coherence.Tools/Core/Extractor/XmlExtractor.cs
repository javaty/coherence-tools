using System;
using System.Xml;
using Tangosol.IO.Pof;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// Implementation of <see cref="IExtractor"/> that extracts the value of 
    /// an attribute or a child element from the target XML node.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    [Serializable]
    public class XmlExtractor : IExtractor, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public XmlExtractor()
        {
        }

        /// <summary>
        /// Construct <c>XmlExtractor</c> instance.
        /// </summary>
        /// <param name="nodeName">
        /// The name of the attribute or child element to extract.
        /// </param>
        public XmlExtractor(string nodeName)
        {
            this.nodeName = nodeName;
        }

        /// <summary>
        /// Construct <c>XmlExtractor</c> instance.
        /// </summary>
        /// <param name="nodeName">
        /// The name of the attribute or child element to extract.
        /// </param>
        /// <param name="nsUri">
        /// Namespace URI of the attribute or child element to extract.
        /// </param>
        public XmlExtractor(string nodeName, string nsUri)
        {
            this.nodeName = nodeName;
            this.nsUri = nsUri;
        }

        #endregion

        #region IExtractor implementation

        public object Extract(object target)
        {
            if (target == null) 
            {
                return null;
            }

            XmlDocument sourceDoc     = (XmlDocument) target;
            XmlElement  sourceElement = sourceDoc.DocumentElement;
            if (sourceElement != null)
            {
                // for some reason .NET XmlElement.GetAttribute needs to be given
                // an empty string when working with default namespace, while 
                // XmlElement.GetElementsByTagName needs as expected default namespace
                // to be given
                string uri = nsUri ?? string.Empty;
                string result = null;
                if (sourceElement.HasAttribute(nodeName, uri))
                {
                    result = sourceElement.GetAttribute(nodeName, uri);
                }
                else
                {
                    uri = nsUri ?? sourceElement.NamespaceURI;
                    XmlNodeList candidates = sourceElement.GetElementsByTagName(nodeName, uri);
                    if (candidates.Count > 0)
                    {
                        result = candidates[0].InnerText;
                    }
                }
                return result;
            }
            
            return null;
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            nodeName = reader.ReadString(0);
            nsUri    = reader.ReadString(1);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, nodeName);
            writer.WriteString(1, nsUri);
        }

        #endregion

        #region Object methods

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="other">Object to compare this object with.</param>
        /// <returns>True if objects are equal, false otherwise.</returns>
        public bool Equals(XmlExtractor other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.nodeName, nodeName) && Equals(other.nsUri, nsUri);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (XmlExtractor)) return false;
            return Equals((XmlExtractor) obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return ((nodeName != null ? nodeName.GetHashCode() : 0)*397) 
                       ^ (nsUri != null ? nsUri.GetHashCode() : 0);
            }
        }

        public override string ToString()
        {
            return "XmlExtractor{" +
               "nodeName=" + nodeName +
               "nsUri=" + nsUri +
               '}';
        }

        #endregion

        #region Data members

        private string nodeName;

        private string nsUri;

        #endregion
    }
}
