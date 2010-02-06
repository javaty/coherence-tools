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

package com.seovic.core;


import com.seovic.config.Configuration;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Factory class for default implementations.
 *
 * @author Aleksandar Seovic  2009.09.28
 */
@SuppressWarnings("unchecked")
public class Defaults
    {
    /**
     * Singleton constructor.
     */
    private Defaults()
        {
        try
            {
            m_ctorExpression = getConstructor(Configuration.getDefaultExpressionType());
            m_ctorExtractor  = getConstructor(Configuration.getDefaultExtractorType());
            m_ctorUpdater    = getConstructor(Configuration.getDefaultUpdaterType());
            m_ctorCondition  = getConstructor(Configuration.getDefaultConditionType());
            }
        catch (ClassNotFoundException e)
            {
            log.error("Unable to initialize Defaults.", e);
            throw new RuntimeException(e);
            }
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


    // ---- helper methods --------------------------------------------------

    /**
     * Gets a constructor for the specified class that accepts a single String
     * argument.
     *
     * @param type  the class to find the constructor for
     *
     * @return a constructor for the specified class that accepts a single
     *         String argument
     */
    protected Constructor getConstructor(Class type)
        {
        try
            {
            return type.getConstructor(String.class);
            }
        catch (NoSuchMethodException e)
            {
            throw new RuntimeException("Unable to find a constructor that accepts"
                                       + " a single String argument in the "
                                       + type.getName() + " class.", e);
            }
        }


    // ---- data members ----------------------------------------------------

    /**
     * Singleton instance.
     */
    private static final Defaults instance = new Defaults();

    /**
     * Logger instance.
     */
    private static final Log log = LogFactory.getLog(Defaults.class);

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
    }
