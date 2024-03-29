/*
 * Copyright 2009 Aleksandar Seovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seovic.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;


/**
 * Provides centralized access to Coherence Tools configuration info.
 * <p/>
 * You can modify configuration settings by editing
 * <tt>coherence-tools.properties</tt> configuration file, which should
 * be located in the classpath root.
 * 
 * @author Aleksandar Seovic  2010.02.05
 */
public class Configuration
    {
    // ---- constructors ----------------------------------------------------

    /// <summary>
    /// Singleton constructor.
    /// </summary>
    private Configuration()
    {
        m_config = loadConfiguration();
    }


    // ---- public methods --------------------------------------------------

    /**
     * Get a default expression type.
     *
     * @return default expression type
     *
     * @throws ClassNotFoundException  if specified class cannot be found
     */
    public static Class getDefaultExpressionType()
            throws ClassNotFoundException
        {
        return Class.forName(s_instance.m_config.get(EXPRESSION_TYPE));
        }

    /**
     * Get a default extractor type.
     *
     * @return default extractor type
     *
     * @throws ClassNotFoundException  if specified class cannot be found
     */
    public static Class getDefaultExtractorType()
            throws ClassNotFoundException
        {
        return Class.forName(s_instance.m_config.get(EXTRACTOR_TYPE));
        }

    /**
     * Get a default updater type.
     *
     * @return default updater type
     *
     * @throws ClassNotFoundException  if specified class cannot be found
     */
    public static Class getDefaultUpdaterType()
            throws ClassNotFoundException
        {
        return Class.forName(s_instance.m_config.get(UPDATER_TYPE));
        }

    /**
     * Get a default condition type.
     *
     * @return default condition type
     *
     * @throws ClassNotFoundException  if specified class cannot be found
     */
    public static Class getDefaultConditionType()
            throws ClassNotFoundException
        {
        return Class.forName(s_instance.m_config.get(CONDITION_TYPE));
        }

    /**
     * Get the name of the default script language.
     *
     * @return the name of the default script language
     */
    public static String getDefaultScriptLanguage()
        {
        return s_instance.m_config.get(SCRIPT_LANGUAGE);
        }

    /**
     * Get sequence generator type.
     *
     * @return sequence generator type
     *
     * @throws ClassNotFoundException  if specified class cannot be found
     */
    public static Class getSequenceGeneratorType()
            throws ClassNotFoundException
        {
        return Class.forName(s_instance.m_config.get(SEQUENCE_GENERATOR_TYPE));
        }

    /**
     * Get the name of the sequence cache.
     *
     * @return the name of the sequence cache
     */
    public static String getSequenceCacheName()
        {
        return s_instance.m_config.get(SEQUENCE_CACHE_NAME);
        }

    /**
     * Get the name of the job scheduler cache.
     *
     * @return the name of the job scheduler cache
     */
    public static String getSchedulerCacheName()
        {
        return s_instance.m_config.get(SCHEDULER_CACHE_NAME);
        }

    // ---- helper methods --------------------------------------------------

    /// <summary>
    /// Loads default values from a application configuration file.
    /// </summary>
    /// <returns>
    /// Dictionary containing configuration values.
    /// </returns>
    private static Map<String, String> loadConfiguration()
        {
        Map<String, String> props = new HashMap<String, String>();
        props.put(EXPRESSION_TYPE, DEFAULT_EXPRESSION_TYPE);
        props.put(EXTRACTOR_TYPE,  DEFAULT_EXTRACTOR_TYPE);
        props.put(UPDATER_TYPE,    DEFAULT_UPDATER_TYPE);
        props.put(CONDITION_TYPE,  DEFAULT_CONDITION_TYPE);
        props.put(SCRIPT_LANGUAGE, DEFAULT_SCRIPT_LANGUAGE);

        props.put(SEQUENCE_GENERATOR_TYPE, DEFAULT_SEQUENCE_GENERATOR_TYPE);
        props.put(SEQUENCE_CACHE_NAME,     DEFAULT_SEQUENCE_CACHE_NAME);

        try
            {
            Properties config = new Properties();
            config.load(Configuration.class.getClassLoader()
                    .getResourceAsStream("coherence-tools.properties"));
            for (String propertyName : config.stringPropertyNames())
                {
                props.put(propertyName, config.getProperty(propertyName));
                }
            }
        catch (Exception e)
            {
            // should never happen, as default file is embedded within JAR
            s_log.warn("Configuration file coherence-tools.properties"
                     + " is missing. Using hardcoded defaults: \n" + props);
            }
        return props;
    }


    // ---- constants -------------------------------------------------------

    // configuration property keys
    private static final String EXPRESSION_TYPE = "expression.type";
    private static final String EXTRACTOR_TYPE  = "extractor.type";
    private static final String UPDATER_TYPE    = "updater.type";
    private static final String CONDITION_TYPE  = "condition.type";
    private static final String SCRIPT_LANGUAGE = "script.language";

    private static final String SEQUENCE_GENERATOR_TYPE = "sequence.generator.type";
    private static final String SEQUENCE_CACHE_NAME     = "sequence.cache.name";

    private static final String SCHEDULER_CACHE_NAME     = "scheduler.cache.name";

    // default values
    private static final String DEFAULT_EXPRESSION_TYPE = "com.seovic.core.expression.SpelExpression";
    private static final String DEFAULT_EXTRACTOR_TYPE  = "com.seovic.core.extractor.ExpressionExtractor";
    private static final String DEFAULT_UPDATER_TYPE    = "com.seovic.core.updater.ExpressionUpdater";
    private static final String DEFAULT_CONDITION_TYPE  = "com.seovic.core.condition.ExpressionCondition";
    private static final String DEFAULT_SCRIPT_LANGUAGE = "javascript";

    private static final String DEFAULT_SEQUENCE_GENERATOR_TYPE = "com.seovic.identity.SimpleSequenceGenerator";
    private static final String DEFAULT_SEQUENCE_CACHE_NAME     = "sequences";


    // ---- data members ----------------------------------------------------

    /**
     * Singleton instance.
     */
    private static final Configuration s_instance = new Configuration();
    
    /**
     * Logger for this class.
     */
    private static final Log s_log = LogFactory.getLog(Configuration.class);

    /**
     * Configuration settings.
     */
    private final Map<String, String> m_config;
    }
