using System;
using System.Collections;

namespace Seovic.Coherence.Core.Expression
{
    public class GroovyExpression : AbstractExpression
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public GroovyExpression()
        {
        }

        /// <summary>
        /// Construct a <code>GroovyExpression</code> instance.
        /// </summary>
        /// <param name="expression">The script to evaluate.</param>
        public GroovyExpression(string expression) : base(expression)
        {
        }

        #endregion

        #region IExpression implementation

        public override object Evaluate(object target, IDictionary variables)
        {
            throw new NotSupportedException();
        }

        public override void EvaluateAndSet(object target, object value)
        {
            throw new NotSupportedException();
        }

        #endregion
    }
}
