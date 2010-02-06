using System;
using System.Collections;
using System.IO;

namespace Seovic.Core.Expression
{
    /// <summary>
    /// An imlementation of <see cref="IExpression"/> that evaluates specified 
    /// expression using <a href="http://groovy.codehaus.org/" target="_new">
    /// Groovy</a>.
    /// </summary>
    /// <remarks>
    /// <b>Expressions of this type can only be executed within Coherence 
    /// cluster, as there is no .NET equivalent of Groovy.</b>
    /// <p/>
    /// Unlike the expression languages such as OGNL and MVEL, Groovy does not have
    /// a notion of a "root object" for expression evaluation. Because of this, the
    /// target object is bound to a variable called <c>target</c> and must be
    /// referenced explicitly within the expression.
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
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
        /// <param name="expression">The expression to evaluate.</param>
        public GroovyExpression(string expression) : base(expression)
        {
        }

        /// <summary>
        /// Construct a <code>GroovyExpression</code> instance.
        /// </summary>
        /// <param name="script">The script to evaluate.</param>
        public GroovyExpression(TextReader script)
            : base(script.ReadToEnd())
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
