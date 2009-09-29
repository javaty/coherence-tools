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


import java.util.*;
import java.io.Serializable;


/**
 * A container for validation errors.
 * <p/>
 * This class groups validation errors by validator names and allows access to
 * both the complete errors collection and to the errors for a certain
 * validator.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public class DefaultValidationErrors implements ValidationErrors, Serializable {
    // ---- data members ----------------------------------------------------

	private Map<String, List<ErrorMessage>> errorMap = new Hashtable<String, List<ErrorMessage>>();


    // ---- constructors ----------------------------------------------------

    /**
     * Default constructor.
     */
    public DefaultValidationErrors() {}


    // ---- ValidationErrors implementation ---------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
    	return errorMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
	public List<String> getProviders() {
        return new ArrayList<String>(errorMap.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public void addError(String provider, ErrorMessage message) {
        if (provider == null || message == null) {
            throw new IllegalArgumentException("Prover or error message cannot"
                    + " null");
        }
        List<ErrorMessage> errors = errorMap.get(provider);
        if (errors == null) {
            errors = new ArrayList<ErrorMessage>();
            errorMap.put(provider, errors);
        }
        errors.add(message);
    }

    /**
     * {@inheritDoc}
     */
	public void mergeErrors(ValidationErrors errorsToMerge) {
        if (errorsToMerge != null) {
            List<String> providers = errorsToMerge.getProviders();
            for (String provider : providers) {
                List<ErrorMessage> messages = errorsToMerge.getErrors(provider);
                for (ErrorMessage message : messages) {
                    addError(provider,  message);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
	public List<ErrorMessage> getErrors(String provider) {
        List<ErrorMessage> errors = errorMap.get(provider);
        return errors == null ? new ArrayList<ErrorMessage>() : errors;
    }

    /**
     * {@inheritDoc}
     */
    public List<ErrorMessage> getErrors() {
        List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		Set<String> providers = errorMap.keySet();
		for (String provider : providers) {
			List<ErrorMessage> errorsList = errorMap.get(provider);
			if (errorsList != null) {
				errors.addAll(errorsList);
			}
		}
		return errors;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getResolvedErrors(String provider, MessageSource messageSource) {
        List<String>       messages = new ArrayList<String>();
        List<ErrorMessage> errors   = errorMap.get(provider);
        if (errors != null) {
            for (ErrorMessage error : errors) {
                messages.add(error.getMessage(messageSource));
            }
        }
        return messages;
    }


    // ---- getters and setters ---------------------------------------------

    /**
     * Return map containing error messages keyed by provider they belong to.
     *
     * @return map containing error messages keyed by provider they belong to
     */
    public Map<String, List<ErrorMessage>> getErrorMap() {
        return errorMap;
    }
}