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

namespace Seovic.Core
{
    /// <summary>
    /// Generic Command interface.
    /// </summary>
    /// <remarks>
    /// Command are parameterized with a return type for convenience.
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    public interface ICommand<T>
    {
        /// <summary>
        /// Execute command.
        /// </summary>
        /// <returns>
        /// Result of the command execution, if any.
        /// </returns>
        T Execute();
    }
}