using System;
using System.Collections;
using System.IO;
using System.Xml;

using Seovic.Core;
using Seovic.Core.Extractor;

namespace Seovic.Loader.Source
{
    /// <summary>
    /// A <see cref="ISource"/> implementation that reads items to load from an 
    /// XML file.
    /// </summary>
    /// <author>Ivan Cikic  2009.11.16</author>
    public class XmlSource : AbstractBaseSource
    {
        #region Constructors

        /// <summary>
        /// Constructs <b>XmlSource</b> instance.
        /// </summary>
        /// <param name="resourceName">
        /// The name of the XML resource to read items from.
        /// </param>
        public XmlSource(string resourceName)
        {
            m_resourceName = resourceName;
        }

        /// <summary>
        /// Construct a XmlSource instance.
        /// </summary>
        /// <param name="reader">Reader to read XML file with</param>
        public XmlSource(TextReader reader)
        {
            m_reader = reader;
        }

        #endregion

        #region ISource implementation

        /// <summary>
        /// Called by the loader to inform source that the loading process is
        /// about to start.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// preliminary one-time set up before the load starts.
        /// </remarks>
        public override void BeginExport()
        {
            if (m_reader == null)
            {
                m_reader = CreateResourceReader(GetResource(m_resourceName));
            }
        }

        /// <summary>
        /// Called by the loader to inform source that the loading process is
        /// finished.
        /// </summary>
        /// <remarks>
        /// This is a lifecycle method that allows implementations to perform any
        /// necessary cleanup after the load is finished.
        /// </remarks>
        public override void EndExport()
        {
            m_reader.Close();
        }

        /// <summary>
        /// Returns an enumerator that iterates through a collection.
        /// </summary>
        /// <returns>
        /// An <see cref="T:System.Collections.IEnumerator"/> object that can be used to iterate through the collection.
        /// </returns>
        public override IEnumerator GetEnumerator()
        {
            return new XmlEnumerator(m_reader);
        }

        /// <summary>
        /// Create default extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property to create an extractor for</param>
        /// <returns>Property extractor instance</returns>
        protected override IExtractor CreateDefaultExtractor(string propertyName)
        {
            return new XmlExtractor(propertyName);
        }

        #endregion

        #region Inner class: XmlEnumerator

        /// <summary>
        /// Enumerator implementation of CsvSource.
        /// </summary>
        public class XmlEnumerator : IEnumerator
        {
            #region Constructors

            /// <summary>
            /// Construct XmlEnumerator instance.
            /// </summary>
            /// <param name="reader">Reader to use.</param>
            public XmlEnumerator(TextReader reader)
            {
                XmlReaderSettings settings = new XmlReaderSettings();
                settings.IgnoreComments               = true;
                settings.IgnoreProcessingInstructions = true;
                settings.IgnoreWhitespace             = true;
                m_xml = XmlReader.Create(reader, settings);
                InitReader();
                MoveToElement();
            }

            #endregion

            #region IEnumerator implementation

            /// <summary>
            /// Advances the enumerator to the next element of the collection.
            /// </summary>
            /// <returns>
            /// true if the enumerator was successfully advanced to the next 
            /// element; false if the enumerator has passed the end of the collection.
            /// </returns>
            public bool MoveNext()
            {
                // this is necessery for two reasons:
                // 1. skipping document element
                // 2. xml.ReadSubtree() behavior with empty elements
                if (m_xml.NodeType != XmlNodeType.None)
                {
                    m_xml.Read();
                }

                MoveToElement();
                return m_xml.NodeType != XmlNodeType.None;
            }

            /// <summary>
            /// Sets the enumerator to its initial position, which is before 
            /// the first element in the collection.
            /// </summary>
            /// <exception cref="T:System.InvalidOperationException">
            /// Always.
            /// </exception>
            public void Reset()
            {
                throw new NotSupportedException(
                    "XmlEnumerator does not support reset operation");
            }

            /// <summary>
            /// Gets the current element in the collection.
            /// </summary>
            /// <returns>
            /// The current element in the collection.
            /// </returns>
            public object Current
            {
                get 
                {
                    XmlReader subtree = null;
                    try
                    {
                        subtree = m_xml.ReadSubtree();
                        return ToDocument(subtree);
                    } 
                    finally
                    {
                        if (subtree != null)
                        {
                            subtree.Close();
                        }
                    }
                }
            }

            #endregion

            #region Helper methods

            /// <summary>
            /// Positions XML reader to next element in the stream or end of stream,
            /// whatever comes first.
            /// </summary>
            private void MoveToElement()
            {
                // move to next element or end of document
                while (m_xml.NodeType != XmlNodeType.Element && m_xml.NodeType != XmlNodeType.None)
                {
                    m_xml.Read();
                }
            }

            /// <summary>
            /// Initialize XML reader.
            /// </summary>
            /// <remarks>
            /// When called after XML reader creation, move reader to first node in the stream.
            /// </remarks>
            private void InitReader()
            {
                m_xml.Read();
            }

            /// <summary>
            /// Create XML Document from provided XML stream.
            /// </summary>
            /// <param name="subtree">
            /// XML stream from which XML Document is constructed.
            /// </param>
            /// <returns>Constructed XML Document.</returns>
            private static XmlDocument ToDocument(XmlReader subtree)
            {
                XmlDocument document = new XmlDocument();
                document.Load(subtree);
                return document;
            }

            #endregion

            #region Data members

            private readonly XmlReader m_xml;

            #endregion
        }

        #endregion

        #region Data members

        private string     m_resourceName;

        private TextReader m_reader;

        #endregion
    }
}