using System;
using Tangosol.IO.Pof;

namespace Seovic.Core.Condition
{
    /// <summary>
    /// An imlementation of <see cref="ICondition"/> that evaluates boolean expression
    /// using one of the <see cref="IExpression"/> implementations.
    /// </summary>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    /// <author>Ivan Cikic  2010.02.05</author>
    [Serializable]
    public class ExpressionCondition : ICondition, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ExpressionCondition()
        {
        }

        /// <summary>
        /// Construct an <tt>ExpressionCondition</tt> instance.
        /// </summary>
        /// <param name="expression">Expression to use.</param>
        public ExpressionCondition(String expression) : this(Defaults.CreateExpression(expression))
        {
        }

        /// <summary>
        /// Construct an <tt>ExpressionCondition</tt> instance.
        /// </summary>
        /// <param name="expression">Expression to use.</param>
        public ExpressionCondition(IExpression expression)
        {
            m_expression = expression;
        }

        #endregion

        #region ICondition implementation

        public bool Evaluate(object target)
        {
            try
            {
                return (bool) m_expression.Evaluate(target);
            }
            catch (InvalidCastException)
            {
                throw new ArgumentException(
                        "Specified expression does not evaluate to a boolean value");
            }
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_expression = (IExpression) reader.ReadObject(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, m_expression);
        }

        #endregion

        #region Object methods

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
            ExpressionCondition condition = (ExpressionCondition) obj;
            return m_expression.Equals(condition.m_expression);
        }

        public override int GetHashCode()
        {
            return m_expression.GetHashCode();
        }

        public override string ToString()
        {
            return "ExpressionCondition{" +
               "expression=" + m_expression +
               '}';
        }

        #endregion

        #region Data members

        /// <summary>
        /// The expression to use.
        /// </summary>
        private IExpression m_expression;

        #endregion
    }
}
