using Seovic.Core.Expression;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// An imlementation of <see cref="IExtractor"/> that extracts value 
    /// from a target object using <see cref="MvelExpression"/>.
    /// </summary>
    public class MvelExtractor : ExpressionExtractor
    {
        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public MvelExtractor()
        {
        }

        /// <summary>
        /// Construct an <c>MvelExtractor</c> instance.
        /// </summary>
        /// <param name="expression">The expression to evaluate.</param>
        public MvelExtractor(string expression)
            : base(new MvelExpression(expression))
        {
        }
    }
}