using System.IO;
using Seovic.Core.Expression;

namespace Seovic.Core.Extractor
{
    /// <summary>
    /// An imlementation of <see cref="IExtractor"/> that extracts value 
    /// from a target object using <see cref="ScriptExpression"/>.
    /// </summary>
    public class ScriptExtractor : ExpressionExtractor
    {
        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ScriptExtractor()
        {
        }

        /// <summary>
        /// Construct an <c>ScriptExtractor</c> instance.
        /// </summary>
        /// <param name="expression">The expression to evaluate.</param>
        public ScriptExtractor(string expression)
            : base(new ScriptExpression(expression))
        {
        }

        /// <summary>
        /// Construct an <c>ScriptExtractor</c> instance.
        /// </summary>
        /// <param name="script">The script to evaluate.</param>
        public ScriptExtractor(TextReader script)
            : base(new ScriptExpression(script))
        {
        }

        /// <summary>
        /// Construct an <c>ScriptExtractor</c> instance.
        /// </summary>
        /// <param name="expression">The expression to evaluate.</param>
        /// <param name="language">Scripting language to use.</param>
        public ScriptExtractor(string expression, string language)
            : base(new ScriptExpression(expression, language))
        {
        }

        /// <summary>
        /// Construct an <c>ScriptExtractor</c> instance.
        /// </summary>
        /// <param name="script">The script to evaluate.</param>
        /// <param name="language">Scripting language to use.</param>
        public ScriptExtractor(TextReader script, string language)
            : base(new ScriptExpression(script, language))
        {
        }
    }
}