using System;
using Tangosol.IO.Pof;

namespace Seovic.Coherence.Core.Extractor
{
    [Serializable]
    public class ExpressionExtractor : IExtractor, IPortableObject
    {
        #region Constructors

        /// <summary>
        /// Deserialization constructor (for internal use only).
        /// </summary>
        public ExpressionExtractor()
        {
        }

        /// <summary>
        /// Construct an <code>ExpressionExtractor</code> instance.
        /// </summary>
        /// <param name="expression">The expression to use.</param>
        public ExpressionExtractor(string expression) :  this(Defaults.CreateExpression(expression))
        {
        }

        /// <summary>
        /// Construct an <code>ExpressionExtractor</code> instance.
        /// </summary>
        /// <param name="expression">The expression to use.</param>
        public ExpressionExtractor(IExpression expression)
        {
            m_expression = expression;
        }

        #endregion

        #region IExtractor implementation

        public object Extract(object target)
        {
            if (target == null)
            {
                return null;
            }
            return m_expression.Evaluate(target);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_expression = (IExpression)reader.ReadObject(0);
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, m_expression);
        }

        #endregion

        #region Object methods

        public override int GetHashCode()
        {
            return m_expression.GetHashCode();
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

            ExpressionExtractor extractor = (ExpressionExtractor) obj;
            return m_expression.Equals(extractor.m_expression);
        }

        public override string ToString()
        {
            return "ExpressionExtractor{" +
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
