using Seovic.Core.Expression;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// An imlementation of <see cref="IExtractor"/> that extracts value 
    /// from a target object using <see cref="OgnlExpression"/>.
    /// </summary>
    public class OgnlExtractor : ExpressionExtractor
    {
        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public OgnlExtractor()
        {
        }

        /// <summary>
        /// Construct an <c>OgnlExtractor</c> instance.
        /// </summary>
        /// <param name="expression">The expression to evaluate.</param>
        public OgnlExtractor(string expression)
            : base(new OgnlExpression(expression))
        {
        }
    }
}