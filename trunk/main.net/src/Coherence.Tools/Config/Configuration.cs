using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Configuration;

using Common.Logging;

namespace Seovic.Config
{
    /// <summary>
    /// Provides centralized access to Coherence Tools configuration info.
    /// </summary>
    /// <remarks>
    /// You can modify configuration settings by editing 
    /// <c>Coherence.Tools.dll.config</c> configuration file, which should
    /// be colocated in the same directory with <c>Coherence.Tools.dll</c>. 
    /// </remarks>
    /// <author>Aleksandar Seovic  2010.02.05</author>
    public class Configuration
    {
        #region Constructors

        /// <summary>
        /// Singleton constructor.
        /// </summary>
        private Configuration()
        {
            m_config = LoadConfiguration();
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Get a default expression type.
        /// </summary>
        /// <returns>Default expression type.</returns>
        public static Type GetDefaultExpressionType()
        {
            return Type.GetType(Instance.m_config[EXPRESSION_TYPE]);
        }

        /// <summary>
        /// Get a default extractor type.
        /// </summary>
        /// <returns>Default extractor type.</returns>
        public static Type GetDefaultExtractorType()
        {
            return Type.GetType(Instance.m_config[EXTRACTOR_TYPE]);
        }

        /// <summary>
        /// Get a default updater type.
        /// </summary>
        /// <returns>Default updater type.</returns>
        public static Type GetDefaultUpdaterType()
        {
            return Type.GetType(Instance.m_config[UPDATER_TYPE]);
        }

        /// <summary>
        /// Get a default condition type.
        /// </summary>
        /// <returns>Default condition type.</returns>
        public static Type GetDefaultConditionType()
        {
            return Type.GetType(Instance.m_config[CONDITION_TYPE]);
        }

        /// <summary>
        /// Return the name of the default script language.
        /// </summary>
        /// <returns>
        /// The name of the default script language.
        /// </returns>
        public static string GetDefaultScriptLanguage()
        {
            return Instance.m_config[SCRIPT_LANGUAGE];
        }

        /// <summary>
        /// Get sequence generator type.
        /// </summary>
        /// <returns>Sequence generator type.</returns>
        public static Type GetSequenceGeneratorType()
        {
            return Type.GetType(Instance.m_config[SEQUENCE_GENERATOR_TYPE]);
        }

        /// <summary>
        /// Return the name of the sequence cache.
        /// </summary>
        /// <returns>
        /// The name of the sequence cache.
        /// </returns>
        public static string GetSequenceCacheName()
        {
            return Instance.m_config[SEQUENCE_CACHE_NAME];
        }

        #endregion

        #region Helper methods

        /// <summary>
        /// Loads default values from a application configuration file.
        /// </summary>
        /// <returns>
        /// Dictinary containing configuration values.
        /// </returns>
        private static IDictionary<string, string> LoadConfiguration()
        {
            IDictionary<string, string> props = new Dictionary<string, string>();
            props.Add(EXPRESSION_TYPE, DEFAULT_EXPRESSION_TYPE);
            props.Add(EXTRACTOR_TYPE,  DEFAULT_EXTRACTOR_TYPE);
            props.Add(UPDATER_TYPE,    DEFAULT_UPDATER_TYPE);
            props.Add(CONDITION_TYPE,  DEFAULT_CONDITION_TYPE);
            props.Add(SCRIPT_LANGUAGE, DEFAULT_SCRIPT_LANGUAGE);

            props.Add(SEQUENCE_GENERATOR_TYPE, DEFAULT_SEQUENCE_GENERATOR_TYPE);
            props.Add(SEQUENCE_CACHE_NAME,     DEFAULT_SEQUENCE_CACHE_NAME);

            try
            {
                NameValueCollection config = ConfigurationManager.AppSettings;
                foreach (string key in config.Keys)
                {
                    props[key] = config[key];
                }
            }
            catch (Exception)
            {
                // should never happen
                log.Warn("Configuration file Coherence.Tools.dll.config"
                         + " is missing. Using hardcoded defaults: \n" + props);
            }
            return props;
        }

        #endregion

        #region Constants
        
        #region Configuration property keys

        private const string EXPRESSION_TYPE = "expression.type";
        private const string EXTRACTOR_TYPE  = "extractor.type";
        private const string UPDATER_TYPE    = "updater.type";
        private const string CONDITION_TYPE  = "condition.type";
        private const string SCRIPT_LANGUAGE = "script.language";

        private const string SEQUENCE_GENERATOR_TYPE = "sequence.generator.type";
        private const string SEQUENCE_CACHE_NAME     = "sequence.cache.name";

        #endregion

        #region Configuration default values

        private const string DEFAULT_EXPRESSION_TYPE = "Seovic.Core.Expression.SpelExpression, Coherence.Tools";
        private const string DEFAULT_EXTRACTOR_TYPE  = "Seovic.Core.Extractor.ExpressionExtractor, Coherence.Tools";
        private const string DEFAULT_UPDATER_TYPE    = "Seovic.Core.Updater.ExpressionUpdater, Coherence.Tools";
        private const string DEFAULT_CONDITION_TYPE  = "Seovic.Core.Condition.ExpressionCondition, Coherence.Tools";
        private const string DEFAULT_SCRIPT_LANGUAGE = "javascript";

        private const string DEFAULT_SEQUENCE_GENERATOR_TYPE = "Seovic.Core.Coherence.Identity.CoherenceSequenceGenerator, Coherence.Tools";
        private const string DEFAULT_SEQUENCE_CACHE_NAME     = "sequences";

        #endregion

        #endregion

        #region Data members

        /// <summary>
        /// Singleton instance.
        /// </summary>
        private static readonly Configuration Instance = new Configuration();

        /// <summary>
        /// Logger for this class.
        /// </summary>
        private static readonly ILog log = LogManager.GetLogger(typeof(Configuration));

        /// <summary>
        /// Configuration settings.
        /// </summary>
        private readonly IDictionary<string, string> m_config;

        #endregion
    }
}
