using System;
using System.Collections;
using System.Globalization;

using Seovic.Core.Extractor;

using Tangosol.IO.Pof;
using Tangosol.Util;
using Tangosol.Util.Filter;

namespace Seovic.Coherence.Util.Filter
{
    public class StartsWithFilter : ComparisonFilter, IIndexAwareFilter
    {
        private bool m_ignoreCase;

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public StartsWithFilter()
        {
        }

        /**
     * Construct <tt>StartsWithFilter</tt> instance.
     *
     * @param propertyName  the name of the propery to evaluate
     * @param filter        the search string
     * @param ignoreCase    the flag specifying whether case should be ignored
     *                      when comparing strings
     */

        public StartsWithFilter(String propertyName, String filter, bool ignoreCase)
            : this(new PropertyExtractor(propertyName), filter, ignoreCase)
        {
        }

        /**
     * Construct <tt>StartsWithFilter</tt> instance.
     *
     * @param extractor   the property extractor to use
     * @param filter      the search string
     * @param ignoreCase  the flag specifying whether case should be ignored
     *                    when comparing strings
     */

        public StartsWithFilter(IValueExtractor extractor, String filter, bool ignoreCase)
            : base(extractor, filter)
        {
            m_ignoreCase = ignoreCase;
        }


        // ---- ExtractorFilter implementation ----------------------------------

        protected override bool EvaluateExtracted(Object o)
        {
            return IsMatch((String) o);
        }


        // ---- IndexAwareFilter implementation ---------------------------------

        public int CalculateEffectiveness(IDictionary indexes, ICollection keys)
        {
            return CalculateRangeEffectiveness(indexes, keys);
        }

        public IFilter ApplyIndex(IDictionary indexes, ICollection keys)
        {
            IMapIndex index = (IMapIndex) indexes[ValueExtractor];
            if (index == null)
            {
                // there is no relevant index
                return this;
            }

            IDictionary candidates = index.IndexContents;
            IList       matches    = new ArrayList();

            foreach (DictionaryEntry indexEntry in candidates)
            {
                String propertyValue = (String) indexEntry.Key;
                if (IsMatch(propertyValue))
                {
                    CollectionUtils.AddAll(matches, (ICollection) indexEntry.Value);
                }
            }

            CollectionUtils.RetainAll(keys, matches);
            return null;
        }


        // ---- helper methods --------------------------------------------------

        /**
     * Return filter string.
     *
     * @return filter string
     */

        protected String FilterString
        {
            get { return (String) Value; }
        }

        /**
     * Return <tt>true</tt> if the specified value matches this filter.
     *
     * @param value  value to check for a match
     *
     * @return <tt>true</tt> if the specified value matches this filter,
     *         <tt>false</tt> otherwise
     */

        protected bool IsMatch(String value)
        {
            return value.StartsWith(FilterString, m_ignoreCase, CultureInfo.InvariantCulture);
        }


        // ---- PortableObject implementation -----------------------------------

        public override void ReadExternal(IPofReader reader)
        {
            base.ReadExternal(reader);

            m_ignoreCase = reader.ReadBoolean(2);
        }

        public override void WriteExternal(IPofWriter writer)
        {
            base.WriteExternal(writer);

            writer.WriteBoolean(2, m_ignoreCase);
        }


        // ---- Object methods implementation -----------------------------------

        public bool Equals(StartsWithFilter obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return base.Equals(obj)
                   && obj.m_ignoreCase.Equals(m_ignoreCase);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            return Equals(obj as StartsWithFilter);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return (base.GetHashCode()*397) ^ m_ignoreCase.GetHashCode();
            }
        }

        public override String ToString()
        {
            return "StartsWithFilter{" +
                   "extractor=" + ValueExtractor +
                   ", filterString=" + FilterString +
                   ", ignoreCase=" + m_ignoreCase +
                   '}';
        }
    }
}