/*
 Copyright 2009 Aleksandar Seovic
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

using System.Collections;

namespace Seovic.Core
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
