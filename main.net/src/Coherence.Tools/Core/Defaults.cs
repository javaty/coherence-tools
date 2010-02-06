using System;
using System.Reflection;

using Configuration=Seovic.Config.Configuration;

namespace Seovic.Core
{
    /// <summary>
    /// Centralized factory for creation of default expression,
    /// extractor, updater and condition instances.
    /// </summary>
    /// <remarks>
    /// You can modify these defaults by editing library configuration file.
    /// See <see cref="Configuration"/> class for details. 
    /// </remarks>
    /// <seealso cref="Configuration"/>
    /// <author>Ivan Cikic  2010.02.05</author>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    public class Defaults
    {
        #region Constructors

        /// <summary>
        /// Singleton constructor.
        /// </summary>
        private Defaults()
        {
            m_ctorExpression = GetConstructor(Configuration.GetDefaultExpressionType());
            m_ctorExtractor  = GetConstructor(Configuration.GetDefaultExtractorType());
            m_ctorUpdater    = GetConstructor(Configuration.GetDefaultUpdaterType());
            m_ctorCondition  = GetConstructor(Configuration.GetDefaultConditionType());
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Creates a default expression instance.
        /// </summary>
        /// <param name="expression">Stirng expression.</param>
        /// <returns>Expression intance</returns>
        public static IExpression CreateExpression(string expression)
        {
            return (IExpression) Instance.m_ctorExpression.Invoke(new[] {expression});
        }

        /// <summary>
        /// Creates a default extractor instance.
        /// </summary>
        /// <param name="expression">String expression</param>
        /// <returns>Extractor instance</returns>
        public static IExtractor CreateExtractor(string expression)
        {
            return (IExtractor) Instance.m_ctorExtractor.Invoke(new[] {expression});
        }

        /// <summary>
        /// Creates a default updater instance.
        /// </summary>
        /// <param name="expression">String expression.</param>
        /// <returns>Updater instance.</returns>
        public static IUpdater CreateUpdater(string expression)
        {
            return (IUpdater) Instance.m_ctorUpdater.Invoke(new[] {expression});
        }

        /// <summary>
        /// Creates a default condition instance.
        /// </summary>
        /// <param name="expression">Conditional expression</param>
        /// <returns>Condition instance</returns>
        public static ICondition CreateCondition(String expression)
        {
            return (ICondition) Instance.m_ctorCondition.Invoke(new[] {expression});
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Gets a constructor for the specified type that accepts a 
        /// single String argument.
        /// </summary>
        /// <param name="type">
        /// The type to find the constructor for.
        /// </param>
        /// <returns>
        /// A constructor for the specified type that accepts a
        /// single String argument.
        /// </returns>
        private static ConstructorInfo GetConstructor(Type type)
        {
            return type.GetConstructor(new[] {typeof(string)});
        }

        #endregion

        #region Data members

        /// <summary>
        /// Singleton instance.
        /// </summary>
        private static readonly Defaults Instance = new Defaults();

        /// <summary>
        /// Constructor for default expression type.
        /// </summary>
        private readonly ConstructorInfo m_ctorExpression;

        /// <summary>
        /// Constructor for default extractor type.
        /// </summary>
        private readonly ConstructorInfo m_ctorExtractor;

        /// <summary>
        /// Constructor for default updater type.
        /// </summary>
        private readonly ConstructorInfo m_ctorUpdater;

        /// <summary>
        /// Constructor for default condition type.
        /// </summary>
        private readonly ConstructorInfo m_ctorCondition;

        #endregion
    }
}
