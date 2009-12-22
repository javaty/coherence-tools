using System;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;
using System.Xml;

using Seovic.Coherence.Core;
using Seovic.Coherence.Core.Updater;

namespace Seovic.Coherence.Loader.Target
{
    public class XmlTarget : AbstractBaseTarget
    {
        #region Constructors

        /// <summary>
        /// Construct XmlTarget instance.
        /// </summary>
        /// <param name="writer">Writer to use.</param>
        /// <param name="rootElementName">Root element name.</param>
        /// <param name="itemElementName">Item element name.</param>
        /// <param name="propertyNames">
        /// Comma-separated property names (any property prefixed with '@'
        /// will be written out as attribute).
        /// </param>
        public XmlTarget(TextWriter writer,
                         string rootElementName,
                         string itemElementName,
                         string propertyNames)
            : this(writer, null, rootElementName, itemElementName, propertyNames)
        {
        }

        /// <summary>
        /// Construct XmlTarget instance.
        /// </summary>
        /// <param name="writer">Writer to use.</param>
        /// <param name="rootElementName">Root element name.</param>
        /// <param name="itemElementName">Item element name.</param>
        /// <param name="propertyNames">
        /// Property names (any property prefixed with '@'
        /// will be written out as attribute).
        /// </param>
        public XmlTarget(TextWriter writer,
                         string rootElementName,
                         string itemElementName,
                         string[] propertyNames)
            : this(writer, null, rootElementName, itemElementName, propertyNames)
        {
        }

        /// <summary>
        /// Construct XmlTarget instance.
        /// </summary>
        /// <param name="writer">Writer to use.</param>
        /// <param name="namespaces">Namespaces.</param>
        /// <param name="rootElementName">Root element name.</param>
        /// <param name="itemElementName">Item element name.</param>
        /// <param name="propertyNames">
        /// Comma-separated property names (any property prefixed with '@'
        /// will be written out as attribute).
        /// </param>
        public XmlTarget(TextWriter writer,
                         IDictionary<string, string> namespaces,
                         string rootElementName,
                         string itemElementName,
                         string propertyNames)
            : this(writer, namespaces, rootElementName, itemElementName, propertyNames.Split(','))
        {
        }

        /// <summary>
        /// Construct XmlTarget instance.
        /// </summary>
        /// <param name="writer">Writer to use.</param>
        /// <param name="namespaces">Namespaces.</param>
        /// <param name="rootElementName">Root element name.</param>
        /// <param name="itemElementName">Item element name.</param>
        /// <param name="propertyNames">
        /// Property names (any property prefixed with '@' will be written
        /// out as attribute).
        /// </param>
        public XmlTarget(TextWriter writer,
                         IDictionary<string, string> namespaces,
                         string rootElementName,
                         string itemElementName,
                         string[] propertyNames)
        {
            m_writer = writer;
            m_nsDict = namespaces == null
                                 ? new Dictionary<string, string>()
                                 : namespaces;
            m_rootElementName = rootElementName;
            m_itemElementName = itemElementName;
            InitAttributesAndElements(propertyNames);
        }


        #endregion

        #region ITarget implementation

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        public override void BeginImport()
        {
            if (m_xmlWriter == null)
            {
                m_xmlWriter = XmlWriter.Create(m_writer);
            }
            m_xmlWriter.WriteStartDocument();

            // write default namespace, if defined
            string defaultNs;
            if (m_nsDict.TryGetValue(string.Empty, out defaultNs))
            {
                m_xmlWriter.WriteStartElement(string.Empty, m_rootElementName, defaultNs);
            }
            else
            {
                m_xmlWriter.WriteStartElement(m_rootElementName);
            }
            foreach (KeyValuePair<string, string> entry in m_nsDict)
            {
                if (!string.Empty.Equals(entry.Key))
                {
                    m_xmlWriter.WriteAttributeString("xmlns", entry.Key, null, entry.Value);
                }
            }
        }

        /// <summary>
        /// Called by the loader to inform target that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        public override void EndImport()
        {
            m_xmlWriter.WriteEndElement();
            m_xmlWriter.WriteEndDocument();
            m_xmlWriter.Close();
        }

        /// <summary>
        /// Import a single item.
        /// </summary>
        /// <param name="item">Item to import.</param>
        public override void ImportItem(object item)
        {
            XmlWriter                   writer = m_xmlWriter;
            IDictionary<string, string> nsDict = m_nsDict;

            writer.WriteStartElement(m_itemElementName);
            IDictionary<string, object> convertedItem = (IDictionary<string, object>)item;
            foreach (Property property in m_attributes)
            {
                string prefix    = property.NamespacePrefix;
                string localName = property.LocalName;
                string ns;
                nsDict.TryGetValue(prefix, out ns);
                string value = convertedItem[localName].ToString();
                writer.WriteAttributeString(prefix, localName, ns, value);
            }
            foreach (Property property in m_elements)
            {
                string prefix    = property.NamespacePrefix;
                string localName = property.LocalName;
                string ns;
                nsDict.TryGetValue(prefix, out ns);
                string value = convertedItem[localName].ToString();
                writer.WriteStartElement(prefix, localName, ns);
                writer.WriteString(value);
                writer.WriteEndElement();
            }
            writer.WriteEndElement();
        }

        /// <summary>
        /// Create an instance of a target object.
        /// </summary>
        /// <param name="source">
        /// <see cref="ISource"/> that object is loaded from.
        /// </param>
        /// <param name="sourceItem">
        /// Source object, in a format determined by its source.
        /// </param>
        /// <returns>
        /// A target object instance.
        /// </returns>
        public override object CreateTargetInstance(ISource source, object sourceItem)
        {
            return new Dictionary<string, object>();
        }

        /// <summary>
        /// Return target property names.
        /// </summary>
        /// <remarks>
        /// Because it is ultimately the target that determines what needs to be
        /// loaded, this method provides target implementations a way to control
        /// which properties from the source they care about.
        /// </remarks>
        public override string[] PropertyNames
        {
            get 
            {
                string[] propertyNames = new string[m_attributes.Count + m_elements.Count];
                int      index         = 0;
                foreach (Property property in m_attributes)
                {
                    propertyNames[index++] = property.LocalName;
                }
                foreach (Property property in m_elements)
                {
                    propertyNames[index++] = property.LocalName;
                }
                return propertyNames;
            }
        }

        #endregion

        #region AbstractBaseTarget implementation

        protected override IUpdater CreateDefaultUpdater(string propertyName)
        {
            return new MapUpdater(propertyName);
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Parses user-specified property names and determines which 
        /// properties should be written out as attributes and which as child
        /// elements.
        /// </summary>
        /// <param name="propertyNames">Property names.</param>
        private void InitAttributesAndElements(string[] propertyNames)
        {
            foreach (string propertyName in propertyNames)
            {
                Property property = new Property(propertyName);
                if (property.IsAttribute())
                {
                    m_attributes.Add(property);
                }
                else
                {
                    m_elements.Add(property);
                }
            }
        }

        #endregion

        #region Data members

        /// <summary>
        /// A writer to use.
        /// </summary>
        private TextWriter m_writer;

        /// <summary>
        /// XML stream writer to use.
        /// </summary>
        private XmlWriter m_xmlWriter;

        /// <summary>
        /// Root element name.
        /// </summary>
        private string m_rootElementName;

        /// <summary>
        /// Item element name.
        /// </summary>
        private string m_itemElementName;

        /// <summary>
        /// A list of property names that should be written as attributes.
        /// </summary>
        private IList<Property> m_attributes = new List<Property>();

        /// <summary>
        /// A list of property names that should be written as child elements.
        /// </summary>
        private List<Property> m_elements = new List<Property>();

        /// <summary>
        /// Namespace dictionary.
        /// </summary>
        private IDictionary<string, string> m_nsDict;

        #endregion

        #region Inner struct: Property

        public struct Property
        {
            #region Constructors

            public Property(string expression)
            {
                Match m = Regex.Match(expression);
                if (m.Success)
                {
                    _prefix      = m.Groups[2].Value;
                    _isAttribute = !string.IsNullOrEmpty(m.Groups[3].Value);
                    _localName   = m.Groups[4].Value;
                } else
                {
                    throw new ArgumentException("Property name must be in the [ns:][@]name format.");
                }
            }

            #endregion

            #region Properties

            public string LocalName
            {
                get { return _localName; }
            }

            public string NamespacePrefix
            {
                get { return _prefix; }
            }

            public bool IsAttribute()
            {
                return _isAttribute;
            }

            public bool IsElement()
            {
                return !_isAttribute;
            }

            #endregion

            #region Static members

            /// <summary>
            /// Regex expression to use when parsing property names.
            /// </summary>
            private static readonly Regex Regex = new Regex("((.*):)?(@)?(.+)");

            #endregion

            #region Data members

            /// <summary>
            /// Namespace prefix.
            /// </summary>
            private String _prefix;

            /// <summary>
            /// True if this property shold be writtin out as attribute.
            /// </summary>
            private bool _isAttribute;

            /// <summary>
            /// Property name.
            /// </summary>
            private String _localName;

            #endregion

        }

        #endregion
    }
}