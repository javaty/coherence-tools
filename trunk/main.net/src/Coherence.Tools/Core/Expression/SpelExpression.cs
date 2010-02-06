using System;
using System.Collections;
using Spel=Spring.Expressions;

namespace Seovic.Core.Expression
{
    /// <summary>
    /// An imlementation of <see cref="IExpression"/> that evaluates specified expression using 
    /// <a href="http://www.springframework.net/doc-latest/reference/html/expressions.html" target="_new">
    /// Spring.NET Expression Language (SpEL)</a>.
    /// </summary>
    /// <remarks>
    /// <b>Expressions of this type can be executed both on the client and within Coherence cluster.</b>
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
    public class SpelExpression : AbstractExpression
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public SpelExpression()
        {
        }

        /// <summary>
        /// Construct a <code>SpelExpression</code> instance.
        /// </summary>
        /// <param name="expression">The script to evaluate.</param>
        public SpelExpression(string expression)
            : base(expression)
        {
        }

        #endregion

        #region IExpression implementation

        /// <summary>
        /// Evaluates expression against the target object.
        /// </summary>
        /// <param name="target">object to evaluate expression against</param><param name="variables">variables to use during evaluation</param>
        /// <returns>
        /// expression result
        /// </returns>
        public override object Evaluate(object target, IDictionary variables)
        {
            Spel.IExpression expression = GetParsedExpression();
            return expression.GetValue(target, variables);
        }

        /// <summary>
        /// Evaluates expression against the target object and sets the 
        ///  last element returned to a specified value.
        /// </summary>
        /// <param name="target">Object to evaluate expression against.
        ///  </param><param name="value">Value to set last element of an expression to.
        ///  </param>
        public override void EvaluateAndSet(object target, object value)
        {
            Spel.IExpression expression = GetParsedExpression();
            expression.SetValue(target, value);
        }

        #endregion

        #region Helper methods

        protected Spel.IExpression GetParsedExpression()
        {
            Spel.IExpression parsedExpression = m_parsedExpression;
            if (parsedExpression == null)
            {
                try
                {
                    m_parsedExpression = parsedExpression =
                                         Spel.Expression.Parse(m_expression);
                }
                catch (Exception)
                {
                    throw new ArgumentException("[" + m_expression +
                                                "] is not a valid SpEL expression");
                }
            }
            return parsedExpression;
        }

        #endregion

        #region Data members

        [NonSerialized]
        private Spel.IExpression m_parsedExpression;

        #endregion
    }
}