using System;
using System.Collections;
using Tangosol.IO.Pof;

namespace Seovic.Core.Expression
{
    /// <summary>
    /// Abstract base class for <see cref="IExpression"/> implementations.
    /// </summary>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
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
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (AbstractExpression)) return false;
            return Equals((AbstractExpression) obj);
        }

        /// <summary>
        /// Test objects for equality.
        /// </summary>
        /// <param name="other">Object to compare this object with.</param>
        /// <returns>True if objects are equal, false otherwise.</returns>
        public bool Equals(AbstractExpression other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_expression, m_expression);
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
