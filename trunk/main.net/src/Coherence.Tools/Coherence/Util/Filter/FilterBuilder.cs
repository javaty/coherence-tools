using System;
using System.Collections;
using System.Collections.Generic;

using Seovic.Core;
using Tangosol.Util;
using Tangosol.Util.Filter;

namespace Seovic.Coherence.Util.Filter
{
    /// <summary>
    /// Helper class that simplifies Coherence filter creation.
    /// </summary>
    /// <author>Aleksandar Seovic  2008.12.27</author>
    /// <author>Ivan Cikic  2009.09.29</author>
    public class FilterBuilder
    {

        #region Builder methods

        /// <summary>
        /// Add EqualsFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Equals(string expression, object value)
        {
            return Equals(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add EqualsFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Equals(IExtractor extractor, object value)
        {
            filters.Add(new EqualsFilter(extractor, value));
            return this;
        }

        /// <summary>
        /// Add NotEqualsFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder NotEquals(string expression, object value)
        {
            return NotEquals(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add NotEqualsFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder NotEquals(IExtractor extractor, object value)
        {
            filters.Add(new NotEqualsFilter(extractor, value));
            return this;
        }

        /// <summary>
        /// Add GreaterFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Greater(String expression, IComparable value)
        {
            return Greater(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add GreaterFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Greater(IExtractor extractor, IComparable value)
        {
            filters.Add(new GreaterFilter(extractor, value));
            return this;
        }

        /// <summary>
        /// Add GreaterEqualsFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder GreaterEquals(string expression, IComparable value)
        {
            return GreaterEquals(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add GreaterEqualsFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder GreaterEquals(IExtractor extractor, IComparable value)
        {
            filters.Add(new GreaterEqualsFilter(extractor, value));
            return this;
        }

        /// <summary>
        /// Add LessFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Less(string expression, IComparable value)
        {
            return Less(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add LessFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Less(IExtractor extractor, IComparable value)
        {
            filters.Add(new LessFilter(extractor, value));
            return this;
        }

        /// <summary>
        /// Add LessEqualFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder LessEquals(string expression, IComparable value)
        {
            return LessEquals(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add LessEqualFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="value">
        /// Object to compare the result of expression with.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder LessEquals(IExtractor extractor, IComparable value)
        {
        filters.Add(new LessEqualsFilter(extractor, value));
        return this;
        }

        /// <summary>
        /// Add InFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="values">
        /// A collection of objects the result of expression should be checked against.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder In(string expression, ICollection values)
        {
            return In(CreateExtractor(expression), values);
        }

        /// <summary>
        /// Add InFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="values">
        /// A collection of objects the result of expression should be checked against.
        /// </param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder In(IExtractor extractor, ICollection values)
        {
            filters.Add(new InFilter(extractor, values));
            return this;
        }

        /// <summary>
        /// Add ContainsFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate (should return collection).</param>
        /// <param name="value">Object to look for in the extractor result</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Contains(string expression, object value)
        {
            return Contains(CreateExtractor(expression), value);
        }

        /// <summary>
        /// Add ContainsFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use (should return collection).</param>
        /// <param name="value">Object to look for in the extractor result</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Contains(IExtractor extractor, object value)
        {
            filters.Add(new ContainsFilter(extractor, value));
            return this;
        }

        /// <summary>
        /// Add ContainsAllFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate (should return collection).</param>
        /// <param name="values">Objects to look for in the extractor result</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder ContainsAll(string expression, ICollection values)
        {
            return ContainsAll(CreateExtractor(expression), values);
        }

        /// <summary>
        /// Add ContainsAllFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use (should return collection).</param>
        /// <param name="values">Objects to look for in the extractor result</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder ContainsAll(IExtractor extractor, ICollection values)
        {
            filters.Add(new ContainsAllFilter(extractor, values));
            return this;
        }

        /// <summary>
        /// Add ContainsAnyFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate (should return collection).</param>
        /// <param name="values">Objects to look for in the extractor result</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder ContainsAny(string expression, ICollection values)
        {
            return ContainsAny(CreateExtractor(expression), values);
        }

        /// <summary>
        /// Add ContainsAnyFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use (should return collection).</param>
        /// <param name="values">Objects to look for in the extractor result</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder ContainsAny(IExtractor extractor, ICollection values)
        {
            filters.Add(new ContainsAnyFilter(extractor, values));
            return this;
        }

        /// <summary>
        /// Add LikeFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="pattern">Pattern to match the result of extractor against</param>
        /// <param name="ignoreCase">Flag specifying whether to ignore case when matching</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Like(string expression, string pattern, bool ignoreCase)
        {
            return Like(CreateExtractor(expression), pattern, ignoreCase);
        }

        /// <summary>
        /// Add LikeFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="pattern">Pattern to match the result of extractor against</param>
        /// <param name="ignoreCase">Flag specifying whether to ignore case when matching</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Like(IExtractor extractor, string pattern, bool ignoreCase)
        {
            filters.Add(new LikeFilter(extractor, pattern, '\\', ignoreCase));
            return this;
        }

        /// <summary>
        /// Add BetweenFilter to builder.
        /// </summary>
        /// <param name="expression">Expression to evaluate.</param>
        /// <param name="from">Start of the range to check extractor result against</param>
        /// <param name="to">End of the range to check extractor  result against</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Between(string expression, IComparable from, IComparable to)
        {
            return Between(CreateExtractor(expression), from, to);
        }

        /// <summary>
        /// Add BetweenFilter to builder.
        /// </summary>
        /// <param name="extractor">Extractor to use.</param>
        /// <param name="from">Start of the range to check extractor result against</param>
        /// <param name="to">End of the range to check extractor  result against</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Between(IExtractor extractor, IComparable from, IComparable to)
        {
            filters.Add(new BetweenFilter(extractor, from, to));
            return this;
        }

        /// <summary>
        /// Add ICondition to builder.
        /// </summary>
        /// <param name="condition">Condition to evaluate</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Condition(string condition)
        {
            return Condition(Defaults.CreateCondition(condition));
        }

        /// <summary>
        /// Add ICondition to builder.
        /// </summary>
        /// <param name="condition">Condition to evaluate</param>
        /// <returns>this FilterBuilder</returns>
        public FilterBuilder Condition(ICondition condition)
        {
            filters.Add(condition);
            return this;
        }

        /// <summary>
        /// Joins all filters added to this builder using logical AND operator.
        /// </summary>
        /// <returns>Conjunction of all filters within this builder</returns>
        public ICondition ToAnd()
        {
            return new FilterAdapter(new AllFilter(GetFilters()));
        }

        /// <summary>
        /// Joins all filters added to this builder using logical OR operator.
        /// </summary>
        /// <returns>Disjunction of all filters within this builder</returns>
        public ICondition ToOr()
        {
            return new FilterAdapter(new AnyFilter(GetFilters()));
        }


        #endregion

        #region Helper methods

        /// <summary>
        /// Convert internal filter list to array.
        /// </summary>
        /// <returns>Array of filters.</returns>
        protected IFilter[] GetFilters()
        {
            IFilter[] array = new IFilter[filters.Count];
            filters.CopyTo(array, 0);
            return array;
        }

        /// <summary>
        /// Create default extractor.
        /// </summary>
        /// <param name="expression">Expression to create extractor for.</param>
        /// <returns>Extractor for the specified expression.</returns>
        protected IExtractor CreateExtractor(string expression)
        {
            return Defaults.CreateExtractor(expression);
        }

        #endregion

        #region Data members

        /// <summary>
        /// Internal list of filters.
        /// </summary>
        private IList<IFilter> filters = new List<IFilter>();

        #endregion


    }
}
