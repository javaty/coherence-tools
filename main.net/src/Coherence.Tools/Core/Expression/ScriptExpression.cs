using System;
using System.Collections;
using System.IO;
using Seovic.Config;
using Tangosol.IO.Pof;

namespace Seovic.Core.Expression
{
    /// <summary>
    /// An imlementation of <see cref="IExpression"/> that evaluates specified 
    /// expression using any supported scripting language.
    /// </summary>
    /// <remarks>
    /// <b>Expressions of this type can only be executed within Coherence 
    /// cluster, as client-side script evaluation is not currently supported.</b>
    /// <p/>
    /// Unlike the expression languages such as OGNL and MVEL, scripts do not have
    /// a notion of a "root object" for expression evaluation. Because of this, the
    /// target object is bound to a variable called <c>target</c> and must be
    /// referenced explicitly within the script.
    /// </remarks>
    public class ScriptExpression : AbstractExpression
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ScriptExpression()
        {
        }

        /// <summary>
        /// Construct a <code>ScriptExpression</code> instance.
        /// </summary>
        /// <param name="expression">The script to evaluate.</param>
        public ScriptExpression(string expression)
            : this(expression, Configuration.GetDefaultScriptLanguage())
        {
        }

        /// <summary>
        /// Construct a <code>ScriptExpression</code> instance.
        /// </summary>
        /// <param name="script">The script to evaluate.</param>
        public ScriptExpression(TextReader script)
            : this(script.ReadToEnd(), Configuration.GetDefaultScriptLanguage())
        {
        }

        /// <summary>
        /// Construct a <code>ScriptExpression</code> instance.
        /// </summary>
        /// <param name="expression">The script to evaluate.</param>
        /// <param name="language">Scripting language to use.</param>
        public ScriptExpression(string expression, string language) 
            : base(expression)
        {
            m_language = language;
        }

        /// <summary>
        /// Construct a <code>ScriptExpression</code> instance.
        /// </summary>
        /// <param name="script">The script to evaluate.</param>
        /// <param name="language">Scripting language to use.</param>
        public ScriptExpression(TextReader script, string language)
            : base(script.ReadToEnd())
        {
            m_language = language;
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

        #region IPortableObject implementation

        public override void ReadExternal(IPofReader reader)
        {
            base.ReadExternal(reader);
            m_language = reader.ReadString(10);
        }

        public override void WriteExternal(IPofWriter writer)
        {
            base.WriteExternal(writer);
            writer.WriteString(10, m_language);
        }

        #endregion

        #region Data members

        /// <summary>
        /// Default language.
        /// </summary>
        public static readonly String DEFAULT_LANGUAGE = "javascript";

        /// <summary>
        /// Default language.
        /// </summary>
        private String m_language;

        #endregion
    }
}
