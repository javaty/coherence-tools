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


import java.util.List;


/**
 * An interface that validation errors containers have to implement.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public interface ValidationErrors {

    /**
     * Does this instance contain any validation errors?
     *
     * @return <b>true</b> if there are no validation errors
     */
	boolean isEmpty();

    /**
     * Gets the list of all providers.
     *
     * @return list of providers
     */
    List<String> getProviders();

    /**
     * Adds the supplied message to this instance's collection of errors.
     *
     * @param provider  provider that should be used for message grouping;
     *                  can't be null
     * @param message   error message to add
     *
     * @throws IllegalArgumentException if the supplied provider or message is null
     */
    void addError(String provider, ErrorMessage message);

    /**
     * Merges another instance of {@link com.seovic.validation.DefaultValidationErrors}
     * into this one.
     *
     * @param errorsToMerge  the validation errors to merge; can be null
     */
    void mergeErrors(ValidationErrors errorsToMerge);

    /**
     * Gets the list of errors for the supplied error provider.
     *
     * @param provider  provider that should be used for message grouping 
     *
     * @return list of errors for supplied error provider
     */
    List<ErrorMessage> getErrors(String provider);

    /**
     * Gets the list of resolved error messages for the supplied lookup provider.
     * <p/>
     * If there are no errors for the supplied lookup provider, an <b>empty</b>
     * list will be returned.

     * @param provider       error key that was used to group messages
     * @param messageSource  message source to resolve messages against
     *
     * @return a list of resolved error messages for the supplied lookup
     *         provider
     */
    List<String> getResolvedErrors(String provider, MessageSource messageSource);

    /**
     * Gets the list of all errors for each error provider.
     *
     * @return list of all error messages
     */
    List<ErrorMessage> getErrors();
}
