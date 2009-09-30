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
package com.seovic.core.expression;


import java.util.Map;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;

import org.springframework.expression.spel.standard.SpelExpressionParser;

import org.springframework.expression.spel.support.StandardEvaluationContext;


/**
 * An imlementation of {@link Expression} that evaluates specified expression
 * using Spring EL.
 * 
 * @author Ivan Cikic  2009.09.30
 */
@SuppressWarnings("serial")
public class SpelExpression extends AbstractExpression {
	// ---- constructors ----------------------------------------------------
	
	/**
	 * Deserialization constructor (for internal use only).
	 */
	public SpelExpression() 
		{
		}

	/**
	 * Construct a <tt>SpelExpression</tt> instance.
     *
     * @param expression  the expression to evaluate
     */
	public SpelExpression(String expression) 
		{
		super(expression);
		}

	
	// ---- Expression implementation ---------------------------------------
	
	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	@Override
	public Object evaluate(Object target, Map variables) 
		{
		Expression                expression = getParsedExpression();
		StandardEvaluationContext context    = new StandardEvaluationContext();
		context.setRootObject(target);
		if (variables != null) 
			{
			context.setVariables(variables);
			}
		try 
			{
			return expression.getValue(context);
			}
		catch (EvaluationException e) 
			{
				throw new RuntimeException(e);
			}
		}	

	/**
     * {@inheritDoc}
     */
	@Override
	public void evaluateAndSet(Object target, Object value) 
		{
		Expression                expression = getParsedExpression();
		StandardEvaluationContext context    = new StandardEvaluationContext();
		context.setRootObject(target);
		try 
			{
			expression.setValue(context, value);
			}
			catch (EvaluationException e) 
			{
				throw new RuntimeException(e);
			}
		}
	
	
	// ---- helper methods --------------------------------------------------
	
	/**
     * Return a parsed SpEL expression.
     *
     * @return parsed SpEL expression
     */
    protected Expression getParsedExpression()
        {
        Expression parsedExpression = m_parsedExpression;
        if (parsedExpression == null)
            {
            try
                {
            	m_parsedExpression = parsedExpression =
                        m_parser.parseExpression(m_expression);
                }
            catch (ParseException e)
                {
                throw new IllegalArgumentException("[" + m_expression +
                                                   "] is not a valid SpEL expression");
                }
            }
        return parsedExpression;
        }
	
    
    // ---- data members ----------------------------------------------------
    
	private static transient ExpressionParser m_parser = new SpelExpressionParser();
	
	private transient Expression m_parsedExpression;
}
