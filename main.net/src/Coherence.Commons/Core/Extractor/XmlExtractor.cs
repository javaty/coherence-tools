using System;
using System.Xml;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Extractor
{
    [Serializable]
    public class XmlExtractor : IExtractor, IPortableObject
    {
        #region Constructors

        public XmlExtractor()
        {
        }

        public XmlExtractor(string nodeName)
        {
            this.nodeName = nodeName;
        }

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
                string uri = nsUri == null
                               ? sourceElement.NamespaceURI
                               : nsUri;
                string result = null;
                if (sourceElement.HasAttribute(nodeName, uri))
                {
                    result = sourceElement.GetAttribute(nodeName, uri);
                }
                else
                {
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
