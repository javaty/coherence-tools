using System.Collections;

namespace Seovic.Coherence.Core
{
    /// <summary>
    /// Expression interface represents an expression that can be evaluated 
    /// against the target object.
    /// </summary>
    /// <author>Aleksandar Seovic  2009.09.20</author>
    public interface IExpression
    {
        /// <summary>
        /// Evaluates expression against the target object.
        /// </summary>
        /// <param name="target">object to evaluate expression against</param>
        /// <returns>expression result</returns>
        object Evaluate(object target);

        /// <summary>
        /// Evaluates expression against the target object.
        /// </summary>
        /// <param name="target">object to evaluate expression against</param>
        /// <param name="variables">variables to use during evaluation</param>
        /// <returns>expression result</returns>
        object Evaluate(object target, IDictionary variables);

        /// <summary>
        /// Evaluates expression against the target object and sets the 
        /// last element returned to a specified value.
        /// </summary>
        /// <param name="target">
        /// Object to evaluate expression against.
        /// </param>
        /// <param name="value">
        /// Value to set last element of an expression to.
        /// </param>
        void EvaluateAndSet(object target, object value);
    }
}
