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

package com.seovic.validation.actions;


import com.seovic.validation.BaseValidationAction;
import com.seovic.validation.ValidationErrors;
import com.seovic.validation.ErrorMessage;

import com.seovic.core.Extractor;


/**
 * Implementation of {@link com.seovic.validation.ValidationAction} that adds
 * error message to the validation errors container.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.04
 */
public class ErrorMessageAction extends BaseValidationAction {
    // ---- data members ----------------------------------------------------

	private String      messageId;
    private Extractor[] messageParams;
    private String[]    providers;


    // ---- constructors ----------------------------------------------------


    /**
     * Constructs an <tt>ErrorMessageAction</tt>.
     *
     * @param messageId  error message resource identifier
     * @param providers  names of the error providers that this message should
     *                   be added to
     */
    public ErrorMessageAction(String messageId, String... providers) {
        if (messageId == null || "".equals(messageId)) {
            throw new IllegalArgumentException("Message id has to be specified.");
        }
        if (providers == null || providers.length == 0) {
        	throw new  IllegalArgumentException("At least one error provider has to be specified.");
        }
        this.messageId = messageId;
        this.providers = providers;
    }


    // ---- getters and setters ---------------------------------------------

    /**
     * Sets the extractors that should be used to resolve message parameters.
     *
     * @param messageParams  the extractors that should be used to resolve
     *                       message parameters
     */
    public void setMessageParams(Extractor... messageParams) {
        this.messageParams = messageParams;
    }


    // ---- BaseValidationAction overrides ----------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInvalid(Object validationContext, ValidationErrors errors) {
        ErrorMessage error = createErrorMessage(validationContext);
        
        for (String provider : this.providers) {
        	errors.addError(provider.trim(), error);
		}
    }


    // ---- helper methods --------------------------------------------------

    /**
     * Resolve the error message.
     *
     * @param validationContext  validation context to resolve message
     *                           parameters against
     *
     * @return resolved error message
     */
    private ErrorMessage createErrorMessage(Object validationContext) {
        if (messageParams != null && messageParams.length > 0) {
            Object[] parameters = resolveMessageParameters(messageParams, validationContext);
            return new ErrorMessage(messageId, parameters);
        } else {
            return new ErrorMessage(messageId, null);
        }
    }

    /**
     * Resolves the message parameters.
     * 
     * @param messageParams      list of parameters to resolve.
     * @param validationContext  validation context to resolve message
     *                           parameters against
     *
     * @return resolved message parameters
     */
    private Object[] resolveMessageParameters(Extractor[] messageParams, Object validationContext) {
        Object[] parameters = new Object[messageParams.length];
        for (int i = 0; i < messageParams.length; i++) {
            parameters[i] = messageParams[i].extract(validationContext);
        }
        return parameters;
    }
}