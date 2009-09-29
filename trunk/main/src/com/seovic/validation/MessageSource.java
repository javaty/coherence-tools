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

package com.seovic.validation;


import java.util.Locale;


/**
 * Strategy interface for resolving messages, with support for the
 * parameterization and internationalization of such messages.
 *
 * @author ic  2009.06.08
 */
public interface MessageSource {

    /**
     * Resolve the message.
     *
     * @param id  message id to use for lookup
     *
     * @return resolved message
     */
    String getMessage(String id);

    /**
     * Resolve the message.
     *
     * @param id      message id to use for lookup
     * @param locale  locle in which to do the lookup
     *
     * @return resolved message
     */
    String getMessage(String id, Locale locale);

    /**
     * Resolve the message.
     *
     * @param id    message id to use for lookup
     * @param args  used to fill the parameters in message
     *
     * @return resolved message
     */
    String getMessage(String id, Object[] args);

    /**
     * Resolve the message.
     *
     * @param id      message id to use for lookup
     * @param args    used to fill the parameters in message
     * @param locale  locle in which to do the lookup
     *
     * @return resolved message
     */
    String getMessage(String id, Object[] args, Locale locale);
}
