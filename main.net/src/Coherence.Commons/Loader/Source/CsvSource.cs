using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using LumenWorks.Framework.IO.Csv;
using Seovic.Coherence.Core;
using Seovic.Coherence.Core.Extractor;

namespace Seovic.Coherence.Loader.Source
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
            return new MapExtractor(propertyName);
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
                    return ToMap();
                }
            }

            #endregion

            #region Helper methods

            /// <summary>
            /// Creates a name-value map for a single Csv line.
            /// </summary>
            /// <returns></returns>
            protected IDictionary<string, string> ToMap()
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

        private TextReader m_reader;

        #endregion

    }
}
