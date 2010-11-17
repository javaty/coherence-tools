/*
 * Copyright 2009 Aleksandar Seovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seovic.coherence.util.filter;


import com.tangosol.util.Filter;

import com.tangosol.util.filter.*;

import com.seovic.core.Extractor;
import com.seovic.core.Defaults;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;


/**
 * Helper class that simplifies Coherence filter creation.
 *
 * @author Aleksandar Seovic  2008.12.27
 */
public class FilterBuilder
    {

    // ---- constructors ----------------------------------------------------

    public FilterBuilder()
        {
        }


    // ---- builder methods -------------------------------------------------

    /**
     * Add EqualsFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param value       object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder equals(String expression, Object value)
        {
        return equals(createExtractor(expression), value);
        }

    /**
     * Add EqualsFilter to builder.
     *
     * @param extractor  extractor to use
     * @param value      object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder equals(Extractor extractor, Object value)
        {
        filters.add(new EqualsFilter(extractor, value));
        return this;
        }

    /**
     * Add NotEqualsFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param value       object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder notEquals(String expression, Object value)
        {
        return notEquals(createExtractor(expression), value);
        }

    /**
     * Add NotEqualsFilter to builder.
     *
     * @param extractor  extractor to use
     * @param value      object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder notEquals(Extractor extractor, Object value)
        {
        filters.add(new NotEqualsFilter(extractor, value));
        return this;
        }

    /**
     * Add GreaterFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param value       object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder greater(String expression, Comparable value)
        {
        return greater(createExtractor(expression), value);
        }

    /**
     * Add GreaterFilter to builder.
     *
     * @param extractor  extractor to use
     * @param value      object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder greater(Extractor extractor, Comparable value)
        {
        filters.add(new GreaterFilter(extractor, value));
        return this;
        }

    /**
     * Add GreaterEqualsFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param value       object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder greaterEquals(String expression, Comparable value)
        {
        return greaterEquals(createExtractor(expression), value);
        }

    /**
     * Add GreaterEqualsFilter to builder.
     *
     * @param extractor  extractor to use
     * @param value      object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder greaterEquals(Extractor extractor, Comparable value)
        {
        filters.add(new GreaterEqualsFilter(extractor, value));
        return this;
        }

    /**
     * Add LessFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param value       object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder less(String expression, Comparable value)
        {
        return less(createExtractor(expression), value);
        }

    /**
     * Add LessFilter to builder.
     *
     * @param extractor  extractor to use
     * @param value      object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder less(Extractor extractor, Comparable value)
        {
        filters.add(new LessFilter(extractor, value));
        return this;
        }

    /**
     * Add LessEqualsFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param value       object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder lessEquals(String expression, Comparable value)
        {
        return lessEquals(createExtractor(expression), value);
        }

    /**
     * Add LessEqualsFilter to builder.
     *
     * @param extractor  extractor to use
     * @param value      object to compare the result of expression with
     *
     * @return this FilterBuilder
     */
    public FilterBuilder lessEquals(Extractor extractor, Comparable value)
        {
        filters.add(new LessEqualsFilter(extractor, value));
        return this;
        }

    /**
     * Add InFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param values      a set of objects the result of expression should be
     *                    checked against
     *
     * @return this FilterBuilder
     */
    public FilterBuilder in(String expression, Set values)
        {
        return in(createExtractor(expression), values);
        }

    /**
     * Add InFilter to builder.
     *
     * @param extractor  extractor to use
     * @param values     a set of objects the result of extractor should be
     *                   checked against
     *
     * @return this FilterBuilder
     */
    public FilterBuilder in(Extractor extractor, Set values)
        {
        filters.add(new InFilter(extractor, values));
        return this;
        }

    /**
     * Add ContainsFilter to builder.
     *
     * @param expression  expression to evaluate (should return collection)
     * @param value       object to look for in the expression result
     *
     * @return this FilterBuilder
     */
    public FilterBuilder contains(String expression, Object value)
        {
        return contains(createExtractor(expression), value);
        }

    /**
     * Add ContainsFilter to builder.
     *
     * @param extractor  extractor to use (should return collection)
     * @param value      object to look for in the extractor result
     *
     * @return this FilterBuilder
     */
    public FilterBuilder contains(Extractor extractor, Object value)
        {
        filters.add(new ContainsFilter(extractor, value));
        return this;
        }

    /**
     * Add ContainsAllFilter to builder.
     *
     * @param expression  expression to evaluate (should return collection)
     * @param values      objects to look for in the expression result
     *
     * @return this FilterBuilder
     */
    public FilterBuilder containsAll(String expression, Set values)
        {
        return containsAll(createExtractor(expression), values);
        }

    /**
     * Add ContainsAllFilter to builder.
     *
     * @param extractor  extractor to use (should return collection)
     * @param values     objects to look for in the extractor result
     *
     * @return this FilterBuilder
     */
    public FilterBuilder containsAll(Extractor extractor, Set values)
        {
        filters.add(new ContainsAllFilter(extractor, values));
        return this;
        }

    /**
     * Add ContainsAnyFilter to builder.
     *
     * @param expression  expression to evaluate (should return collection)
     * @param values      objects to look for in the expression result
     *
     * @return this FilterBuilder
     */
    public FilterBuilder containsAny(String expression, Set values)
        {
        return containsAny(createExtractor(expression), values);
        }

    /**
     * Add ContainsAnyFilter to builder.
     *
     * @param extractor  extractor to use (should return collection)
     * @param values     objects to look for in the extractor result
     *
     * @return this FilterBuilder
     */
    public FilterBuilder containsAny(Extractor extractor, Set values)
        {
        filters.add(new ContainsAnyFilter(extractor, values));
        return this;
        }

    /**
     * Add LikeFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param pattern     pattern to match the result of expression against
     * @param ignoreCase  flag specifying whether to ignore case when matching
     *
     * @return this FilterBuilder
     */
    public FilterBuilder like(String expression, String pattern, boolean ignoreCase)
        {
        return like(createExtractor(expression), pattern, ignoreCase);
        }

    /**
     * Add LikeFilter to builder.
     *
     * @param extractor   extractor to use
     * @param pattern     pattern to match the result of extractor against
     * @param ignoreCase  flag specifying whether to ignore case when matching
     *
     * @return this FilterBuilder
     */
    public FilterBuilder like(Extractor extractor, String pattern, boolean ignoreCase)
        {
        filters.add(new LikeFilter(extractor, pattern, '\\', ignoreCase));
        return this;
        }

    /**
     * Add BetweenFilter to builder.
     *
     * @param expression  expression to evaluate
     * @param from        start of the range to check expression result against
     * @param to          end of the range to check expression result against
     *
     * @return this FilterBuilder
     */
    public FilterBuilder between(String expression, Comparable from, Comparable to)
        {
        return between(createExtractor(expression), from, to);
        }

    /**
     * Add BetweenFilter to builder.
     *
     * @param extractor  extractor to use
     * @param from       start of the range to check extractor result against
     * @param to         end of the range to check extractor  result against
     *
     * @return this FilterBuilder
     */
    public FilterBuilder between(Extractor extractor, Comparable from, Comparable to)
        {
        filters.add(new BetweenFilter(extractor, from, to));
        return this;
        }

    /**
     * Add Filter to builder.
     *
     * @param expression  condition to evaluate
     *
     * @return this FilterBuilder
     */
    public FilterBuilder filter(String expression)
        {
        return filter(Defaults.createCondition(expression));
        }

    /**
     * Add Filter to builder.
     *
     * @param filter  filter to evaluate
     *
     * @return this FilterBuilder
     */
    public FilterBuilder filter(Filter filter)
        {
        filters.add(filter);
        return this;
        }

    /**
     * Joins all filters added to this builder using logical AND operator.
     *
     * @return conjunction of all filters within this builder
     */
    public Filter all()
        {
        return new AllFilter(getFilters());
        }

    /**
     * Joins all filters added to this builder using logical OR operator.
     *
     * @return disjunction of all filters within this builder
     */
    public Filter any()
        {
        return new AnyFilter(getFilters());
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Convert internal filter list to array.
     *
     * @return array of filters
     */
    protected Filter[] getFilters()
        {
        return filters.toArray(new Filter[filters.size()]);
        }

    /**
     * Create default extractor.
     *
     * @param expression  expression to create extractor for
     *
     * @return default extractor for the specified expression
     */
    protected Extractor createExtractor(String expression)
        {
        return Defaults.createExtractor(expression);
        }


    // ---- data members ----------------------------------------------------

    /**
     * Internal list of filters.
     */
    private List<Filter> filters = new ArrayList<Filter>();
    }
