using System;
using System.Collections.Generic;
using System.Text;
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
            // TODO:
            throw new NotImplementedException();
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

        public override string ToString()
        {
            return "XmlExtractor{" +
               "nodeName=" + nodeName +
               "nsUri=" + nsUri +
               '}'; 
        }

        public override bool Equals(object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            XmlExtractor that = (XmlExtractor)obj;

            return nodeName.Equals(that.nodeName) 
                && !(nsUri != null
                     ? !nsUri.Equals(that.nsUri)
                     : that.nsUri != null);
        }

        public override int GetHashCode()
        {
            int hash = 17;
            hash = hash * 31 + nodeName.GetHashCode();
            hash = hash*31 + (nsUri != null ? nsUri.GetHashCode() : 0);
            return hash;
        }

        #endregion

        #region Data members

        private string nodeName;

        private string nsUri;

        #endregion


    }
}
