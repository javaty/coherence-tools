using System;
using System.Collections;

namespace Seovic.Core.Expression
{
    /// <summary>
    /// An imlementation of <see cref="IExpression"/> that evaluates specified expression
    /// using <a href="http://www.opensymphony.com/ognl/" target="_new">OGNL</a>.
    /// </summary>
    /// <remarks>
    /// <b>Expressions of this type can only be executed within Coherence 
    /// cluster, as there is no .NET equivalent of MVEL.</b>
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
    public class OgnlExpression : AbstractExpression
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public OgnlExpression()
        {
        }

        /// <summary>
        /// Construct a <code>OgnlExpression</code> instance.
        /// </summary>
        /// <param name="expression">The script to evaluate.</param>
        public OgnlExpression(string expression) : base(expression)
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
