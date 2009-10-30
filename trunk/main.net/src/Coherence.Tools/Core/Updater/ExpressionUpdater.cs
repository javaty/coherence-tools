using System;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Updater
{
    [Serializable]
    public class ExpressionUpdater : IUpdater, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ExpressionUpdater()
        {
        }

        /// <summary>
        /// Construct an <code>ExpressionUpdater</code> instance.
        /// </summary>
        /// <param name="expression">
        /// The expression to use.
        /// </param>
        public ExpressionUpdater(string expression)
            : this(Defaults.CreateExpression(expression))
        {
        }

        /// <summary>
        /// Construct an <code>ExpressionUpdater</code> instance.
        /// </summary>
        /// <param name="expression">
        /// The expression to use.
        /// </param>
        public ExpressionUpdater(IExpression expression)
        {
            m_expression = expression;
        }

        #endregion

        #region IUpdater implementation

        public void Update(object target, object value)
        {
            if (target == null)
            {
                throw new ArgumentException("Updater target cannot be null");
            }
            m_expression.EvaluateAndSet(target, value);
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

        public bool Equals(ExpressionUpdater other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_expression, m_expression);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (ExpressionUpdater)) return false;
            return Equals((ExpressionUpdater) obj);
        }

        public override int GetHashCode()
        {
            return m_expression.GetHashCode();
        }

        public override string ToString()
        {
            return "ExpressionUpdater{" +
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
