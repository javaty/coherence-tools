using System;
using System.Collections;
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
        public ExpressionExtractor(IExpression expression) : this(expression, null)
        {
        }

        /// <summary>
        /// Construct an <code>ExpressionExtractor</code> instance.
        /// </summary>
        /// <param name="expression">The expression to use.</param>
        /// <param name="variables">The dictionary containing variables to be used during expression evaluation.</param>
        public ExpressionExtractor(IExpression expression, IDictionary variables)
        {
            m_expression = expression;
            m_variables  = variables;
        }

        #endregion

        #region IExtractor implementation

        public object Extract(object target)
        {
            if (target == null)
            {
                return null;
            }
            return m_expression.Evaluate(target, m_variables);
        }

        #endregion

        #region IPortableObject implementation

        public void ReadExternal(IPofReader reader)
        {
            m_expression = (IExpression)reader.ReadObject(0);
            m_variables  = reader.ReadDictionary(1, new Hashtable());
        }

        public void WriteExternal(IPofWriter writer)
        {
            writer.WriteObject(0, m_expression);
            writer.WriteDictionary(1, m_variables);
        }

        #endregion

        #region Object methods

        public bool Equals(ExpressionExtractor other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Equals(other.m_expression, m_expression);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof (ExpressionExtractor)) return false;
            return Equals((ExpressionExtractor) obj);
        }

        public override int GetHashCode()
        {
            return m_expression.GetHashCode();
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

        /// <summary>
        /// The variables to use during expression evaluation.
        /// </summary>
        private IDictionary m_variables;

        #endregion
    }
}
