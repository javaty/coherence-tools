using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using LumenWorks.Framework.IO.Csv;
using Seovic.Core;
using Seovic.Core.Extractor;

namespace Seovic.Loader.Source
{
    /// <summary>
    ///  A <see cref="ISource"/> implementation that reads items to load from a CSV file.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.06.15</author>
    /// <author>Ivan Cikic  2009.10.01</author>
    public class CsvSource : AbstractBaseSource
    {
        #region Constructors

        /// <summary>
        /// Constructs CsvSource instance.
        /// </summary>
        /// <param name="resourceName">
        /// The name of the CSV resource to read items from.
        /// </param>
        public CsvSource(string resourceName)
        {
            m_resourceName = resourceName;
        }

        /// <summary>
        /// Construct a CsvSource instance.
        /// </summary>
        /// <param name="reader">Reader to read CSV file with</param>
        public CsvSource (TextReader reader)
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
            return new CsvEnumerator(m_reader);
        }

        /// <summary>
        /// Create default extractor for the specified property.
        /// </summary>
        /// <param name="propertyName">Property to create an extractor for</param>
        /// <returns>Property extractor instance</returns>
        protected override IExtractor CreateDefaultExtractor(string propertyName)
        {
            return new DictionaryExtractor(propertyName);
        }

        #endregion

        #region Inner class: CsvEnumerator

        /// <summary>
        /// Enumerator implementation of CsvSource.
        /// </summary>
        public class CsvEnumerator : IEnumerator
        {
            #region Constructors

            /// <summary>
            /// Construct CsvEnumerator instance.
            /// </summary>
            /// <param name="reader">Reader to use.</param>
            public CsvEnumerator(TextReader reader)
            {
                m_csv        = new CsvReader(reader, true);
                m_header     = m_csv.GetFieldHeaders();
                m_fieldCount = m_csv.FieldCount;
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
                return m_csv.ReadNextRecord();
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
                    "CsvEnumerator does not support reset operation");
            }

            /// <summary>
            /// Gets the current element in the collection.
            /// </summary>
            /// <returns>
            /// The current element in the collection.
            /// </returns>
            /// <exception cref="T:System.InvalidOperationException">The enumerator is positioned before the first element of the collection or after the last element.-or- The collection was modified after the enumerator was created.</exception><filterpriority>2</filterpriority>
            public object Current
            {
                get 
                {
                    return ToDictionary();
                }
            }

            #endregion

            #region Helper methods

            /// <summary>
            /// Creates a name-value map for a single Csv line.
            /// </summary>
            /// <returns></returns>
            protected IDictionary<string, string> ToDictionary()
            {
                IDictionary<string, string> dict = new Dictionary<string, string>();
                for (int i = 0, count = m_fieldCount; i < count; i++)
                {
                    string value = m_csv[i];
                    dict[m_header[i]] = value.Length > 0 ? value : null;
                }
                return dict;
            }

            #endregion

            #region Data members

            private CsvReader m_csv;
            private string[]  m_header;
            private int       m_fieldCount;

            #endregion
        }

        #endregion

        #region Data members

        private string     m_resourceName;

        private TextReader m_reader;

        #endregion

    }
}
