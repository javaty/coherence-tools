using System;
using System.Collections;

namespace Seovic.Coherence.Core.Expression
{
    public class MvelExpression : AbstractExpression
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public MvelExpression()
        {
        }

        /// <summary>
        /// Construct a <tt>MvelExpression</tt> instance.
        /// </summary>
        /// <param name="expression">The script to evaluate.</param>
        public MvelExpression(string expression) : base(expression)
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
