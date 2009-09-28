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

package com.seovic.lang;


import java.lang.reflect.Constructor;

import java.util.Properties;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Factory class for default implementations.
 *
 * @author Aleksandar Seovic  2009.09.28
 */
public class Defaults
    {
    /**
     * Singleton constructor.
     */
    @SuppressWarnings("unchecked")
    private Defaults()
        {
        Properties props = loadDefaults();
        m_ctorExpression = getConstructor(props.getProperty(EXPRESSION_TYPE));
        m_ctorExtractor  = getConstructor(props.getProperty(EXTRACTOR_TYPE));
        m_ctorUpdater    = getConstructor(props.getProperty(UPDATER_TYPE));
        m_ctorCondition  = getConstructor(props.getProperty(CONDITION_TYPE));
        m_scriptLanguage = props.getProperty(SCRIPT_LANGUAGE);
        }

    // ---- public methods --------------------------------------------------

    /**
     * Creates a default expression instance.
     *
     * @param expression  string expression
     *
     * @return expression instance
     */
    public static Expression createExpression(String expression)
        {
        try
            {
            return instance.m_ctorExpression.newInstance(expression);
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * Creates a default extractor instance.
     *
     * @param expression  string expression
     *
     * @return extractor instance
     */
    public static Extractor createExtractor(String expression)
        {
        try
            {
            return instance.m_ctorExtractor.newInstance(expression);
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * Creates a default updater instance.
     *
     * @param expression  string expression
     *
     * @return updater instance
     */
    public static Updater createUpdater(String expression)
        {
        try
            {
            return instance.m_ctorUpdater.newInstance(expression);
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * Creates a default condition instance.
     *
     * @param expression  conditional expression
     *
     * @return condition instance
     */
    public static Condition createCondition(String expression)
        {
        try
            {
            return instance.m_ctorCondition.newInstance(expression);
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * Return the name of the default script language.
     *
     * @return the name of the default script language
     */
    public static String getScriptLanguage()
        {
        return instance.m_scriptLanguage;
        }


    // ---- helper methods --------------------------------------------------

    /**
     * Loads default values from a com/seovic/lang/defaults.properties file.
     *
     * @return loaded Properties instance
     */
    protected Properties loadDefaults()
        {
        Properties props = new Properties();
        try
            {
            props.load(Defaults.class.getResourceAsStream("defaults.properties"));
            }
        catch (IOException e)
            {
            // should never happen
            props.setProperty(EXPRESSION_TYPE, DEFAULT_EXPRESSION_TYPE);
            props.setProperty(EXTRACTOR_TYPE,  DEFAULT_EXTRACTOR_TYPE);
            props.setProperty(UPDATER_TYPE,    DEFAULT_UPDATER_TYPE);
            props.setProperty(CONDITION_TYPE,  DEFAULT_CONDITION_TYPE);
            props.setProperty(SCRIPT_LANGUAGE, DEFAULT_SCRIPT_LANGUAGE);

            log.warn("Configuration resource com/seovic/lang/defaults.properties"
                     + " not found. Using hardcoded defaults: " + props);
            }
        return props;
        }

    /**
     * Gets a constructor from the specified class that accepts a single String
     * argument.
     *
     * @param className  the name of the class to find the constructor in
     *
     * @return a constructor for the specified class that accepts a single
     *         String argument
     */
    protected Constructor getConstructor(String className)
        {
        try
            {
            Class cls = Class.forName(className);
            return cls.getConstructor(String.class);
            }
        catch (Exception e)
            {
            throw new RuntimeException("Unable to find a constructor that accepts"
                                       + " a single String argument in the "
                                       + className + " class.", e);
            }
        }


    // ---- constants -------------------------------------------------------

    /**
     * Property names
     */
    private static final String EXPRESSION_TYPE = "expression.type";
    private static final String EXTRACTOR_TYPE  = "extractor.type";
    private static final String UPDATER_TYPE    = "updater.type";
    private static final String CONDITION_TYPE  = "condition.type";
    private static final String SCRIPT_LANGUAGE = "script.language";

    /**
     * Default property values
     */
    private static final String DEFAULT_EXPRESSION_TYPE = "com.seovic.lang.expression.MvelExpression";
    private static final String DEFAULT_EXTRACTOR_TYPE  = "com.seovic.lang.extractor.ExpressionExtractor";
    private static final String DEFAULT_UPDATER_TYPE    = "com.seovic.lang.updater.ExpressionUpdater";
    private static final String DEFAULT_CONDITION_TYPE  = "com.seovic.lang.condition.ExpressionCondition";
    private static final String DEFAULT_SCRIPT_LANGUAGE = "javascript";


    // ---- static members --------------------------------------------------

    /**
     * Singleton instance.
     */
    private static final Defaults instance = new Defaults();

    /**
     * Logger instance.
     */
    private static final Log log = LogFactory.getLog(Defaults.class);


    // ---- data members ----------------------------------------------------

    /**
     * Constructor for default expression type.
     */
    private Constructor<Expression> m_ctorExpression;

    /**
     * Constructor for default extractor type.
     */
    private Constructor<Extractor> m_ctorExtractor;

    /**
     * Constructor for default updater type.
     */
    private Constructor<Updater> m_ctorUpdater;

    /**
     * Constructor for default condition type.
     */
    private Constructor<Condition> m_ctorCondition;

    /**
     * The name of the default script language.
     */
    private String m_scriptLanguage;
    }
