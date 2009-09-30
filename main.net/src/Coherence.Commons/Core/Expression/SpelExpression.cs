using System;
using System.Collections;
using Spel=Spring.Expressions;

namespace Seovic.Coherence.Core.Expression
{
    public class SpelExpression : AbstractExpression
    {
        #region Constructors

        public SpelExpression()
        {
        }

        public SpelExpression(string expression) : base(expression)
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