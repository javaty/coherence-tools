using System;
using System.Collections;

namespace Seovic.Core.Expression
{
    /// <summary>
    /// An imlementation of <see cref="IExpression"/> that evaluates specified 
    /// expression using <a href="http://mvel.codehaus.org/" target="_new">MVEL</a>.
    /// </summary>
    /// <remarks>
    /// <b>Expressions of this type can only be executed within Coherence 
    /// cluster, as there is no .NET equivalent of MVEL.</b>
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
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
