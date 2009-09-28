using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Expression
{
    [Serializable]
    public abstract class AbstractExpression : IExpression, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        protected AbstractExpression()
        {
        }

        /// <summary>
        /// Construct an expression instance.
        /// </summary>
        /// <param name="expression">
        /// The expression to evaluate.
        /// </param>
        protected AbstractExpression(string expression)
        {
            m_expression = expression;
        }

        #endregion

        #region IExpression implementation

        public object Evaluate(object target)
        {
            return Evaluate(target, null);
        }

        public abstract object Evaluate(object target, IDictionary variables);

        public abstract void EvaluateAndSet(object target, object value);

        #endregion

        #region IPortableObject implementation

        public virtual void ReadExternal(IPofReader reader)
        {
            m_expression = reader.ReadString(0);
        }

        public virtual void WriteExternal(IPofWriter writer)
        {
            writer.WriteString(0, m_expression);
        }

        #endregion

        #region Object methods

        public override string ToString()
        {
            return GetType().Name + "{" +
               "expression='" + m_expression + '\'' +
               '}';
        }

        public override bool Equals(object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            AbstractExpression that = (AbstractExpression)obj;
            return m_expression.Equals(that.m_expression);
        }

        public override int GetHashCode()
        {
            return m_expression.GetHashCode();
        }

        #endregion

        #region Data members

        /// <summary>
        /// Expression source.
        /// </summary>
        protected String m_expression;

        #endregion
    }
}
