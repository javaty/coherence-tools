using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Configuration;
using System.Reflection;

namespace Seovic.Coherence.Core
{
    public class Defaults
    {
        #region Constructors

        /// <summary>
        /// Singleton constructor.
        /// </summary>
        private Defaults()
        {
            IDictionary<string, string> dict = LoadDefaults();
            m_ctorExpression = GetConstructor(dict[EXPRESSION_TYPE]);
            m_ctorExtractor  = GetConstructor(dict[EXTRACTOR_TYPE]);
            m_ctorUpdater    = GetConstructor(dict[UPDATER_TYPE]);
            m_ctorCondition  = GetConstructor(dict[CONDITION_TYPE]);
            m_scriptLanguage = dict[SCRIPT_LANGUAGE];
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
    
        /// <summary>
        /// Return the name of the default script language.
        /// </summary>
        /// <returns>
        /// The name of the default script language.
        /// </returns>
        public static string GetScriptLanguage()
        {
            return Instance.m_scriptLanguage;
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Loads default values from a application configuration file.
        /// </summary>
        /// <returns>
        /// Dictinary containing configuration values.
        /// </returns>
        protected IDictionary<string, string> LoadDefaults()
        {
            IDictionary<string, string> props = new Dictionary<string, string>();
            try
            {
                NameValueCollection config = ConfigurationManager.AppSettings;
                foreach (string key in config.Keys)
                {
                    props.Add(key, config[key]);
                }
            }
            catch (Exception e)
            {
                // should never happen
                props.Add(EXPRESSION_TYPE, DEFAULT_EXPRESSION_TYPE);
                props.Add(EXTRACTOR_TYPE,  DEFAULT_EXTRACTOR_TYPE);
                props.Add(UPDATER_TYPE,    DEFAULT_UPDATER_TYPE);
                props.Add(CONDITION_TYPE,  DEFAULT_CONDITION_TYPE);
                props.Add(SCRIPT_LANGUAGE, DEFAULT_SCRIPT_LANGUAGE);
                // TODO: log
                //log.warn("Configuration resource com/seovic/lang/defaults.properties"
                //         + " not found. Using hardcoded defaults: " + props);
            }
            return props;
        }

        /// <summary>
        /// Gets a constructor from the specified class that accepts a 
        /// single String argument.
        /// </summary>
        /// <param name="typeName">
        /// The name of the type to find the constructor in
        /// </param>
        /// <returns>
        /// A constructor for the specified class that accepts a
        /// single String argument
        /// </returns>
        protected ConstructorInfo GetConstructor(string typeName)
        {
            Type cls = Type.GetType(typeName);
            return cls.GetConstructor(new[] {typeof(String)});
        }

        #endregion

        #region Constants
        
        #region Configuration property keys

        private static readonly string EXPRESSION_TYPE = "expression.type";
        private static readonly string EXTRACTOR_TYPE  = "extractor.type";
        private static readonly string UPDATER_TYPE    = "updater.type";
        private static readonly string CONDITION_TYPE  = "condition.type";
        private static readonly string SCRIPT_LANGUAGE = "script.language";

        #endregion

        #region Configuration default values

        // TBD
        private static readonly string DEFAULT_EXPRESSION_TYPE = "Seovic.Coherence.Core.Expression.MvelExpression, Coherence.Commons";
        private static readonly string DEFAULT_EXTRACTOR_TYPE  = "Seovic.Coherence.Core.Extractor.ExpressionExtractor, Coherence.Commons";
        private static readonly string DEFAULT_UPDATER_TYPE    = "Seovic.Coherence.Core.Updater.ExpressionUpdater, Coherence.Commons";
        private static readonly string DEFAULT_CONDITION_TYPE  = "Seovic.Coherence.Core.Condition.ExpressionCondition, Coherence.Commons";
        private static readonly string DEFAULT_SCRIPT_LANGUAGE = "javascript";
        
        #endregion

        #endregion

        #region Static members

        private static readonly Defaults Instance = new Defaults();

        #endregion

        #region Data members

        /// <summary>
        /// Constructor for default expression type.
        /// </summary>
        private ConstructorInfo m_ctorExpression;

        /// <summary>
        /// Constructor for default extractor type.
        /// </summary>
        private ConstructorInfo m_ctorExtractor;

        /// <summary>
        /// Constructor for default updater type.
        /// </summary>
        private ConstructorInfo m_ctorUpdater;

        /// <summary>
        /// Constructor for default condition type.
        /// </summary>
        private ConstructorInfo m_ctorCondition;

        /// <summary>
        /// The name of the default script language.
        /// </summary>
        private String m_scriptLanguage;

        #endregion

    }
}
