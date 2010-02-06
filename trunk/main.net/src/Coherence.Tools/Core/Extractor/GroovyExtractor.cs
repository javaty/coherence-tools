using System.IO;
using Seovic.Core.Expression;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// An imlementation of <see cref="IExtractor"/> that extracts value 
    /// from a target object using <see cref="GroovyExpression"/>.
    /// </summary>
    public class GroovyExtractor : ExpressionExtractor
    {
        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public GroovyExtractor()
        {
        }

        /// <summary>
        /// Construct an <c>GroovyExtractor</c> instance.
        /// </summary>
        /// <param name="expression">The expression to evaluate.</param>
        public GroovyExtractor(string expression)
            : base(new GroovyExpression(expression))
        {
        }

        /// <summary>
        /// Construct an <c>GroovyExtractor</c> instance.
        /// </summary>
        /// <param name="script">The script to evaluate.</param>
        public GroovyExtractor(TextReader script)
            : base(new GroovyExpression(script))
        {
        }
    }
}