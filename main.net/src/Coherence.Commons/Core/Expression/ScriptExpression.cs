using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Expression
{
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
            : this(Defaults.GetScriptLanguage(), expression)
        {
        }

        /// <summary>
        /// Construct a <code>ScriptExpression</code> instance.
        /// </summary>
        /// <param name="language">Scripting language to use.</param>
        /// <param name="expression">The script to evaluate.</param>
        private ScriptExpression(string language, string expression) : base(expression)
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
