using Seovic.Core.Expression;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// An imlementation of <see cref="IExtractor"/> that extracts value 
    /// from a target object using <see cref="SpelExpression"/>.
    /// </summary>
    public class SpelExtractor : ExpressionExtractor
    {
        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public SpelExtractor()
        {
        }

        /// <summary>
        /// Construct an <c>SpelExtractor</c> instance.
        /// </summary>
        /// <param name="expression">The expression to evaluate.</param>
        public SpelExtractor(string expression)
            : base(new SpelExpression(expression))
        {
        }
    }
}