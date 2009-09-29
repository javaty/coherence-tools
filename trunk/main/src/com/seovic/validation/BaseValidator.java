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


import java.util.ArrayList;
import java.util.List;

import com.seovic.core.Condition;
import com.seovic.core.Defaults;
import com.seovic.core.Extractor;

import com.seovic.validation.actions.ErrorMessageAction;


/**
 * Base class that defines common properties for all validators.
 * <p/>
 * Custom validators should always extend this class instead of simply
 * implementing {@link Validator} interface, in order to inherit common
 * validator functionality.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.03
 */
public abstract class BaseValidator implements Validator {
    // ---- data members ----------------------------------------------------

    private List<ValidationAction> actions = new ArrayList<ValidationAction>();
    private Extractor              testExtractor;
    private Condition              whenCondition = Condition.TRUE;


    // ---- constructors ----------------------------------------------------

    /**
     * Creates a new instance of the <tt>BaseValidator</tt> class.
     */
    public BaseValidator() {
        this(Extractor.IDENTITY);
    }

    /**
     * Creates a new instance of the <tt>BaseValidator</tt> class.
     *
     * @param test  the name of the target object's property to validate
     */
    public BaseValidator(String test) {
        this.testExtractor = test != null
                             ? Defaults.createExtractor(test)
                             : Extractor.IDENTITY;
    }

    /**
     * Creates a new instance of the <tt>BaseValidator</tt> class.
     *
     * @param testExtractor  an extractor that will be used to extract a value
     *                       to validate from a target object
     */
    public BaseValidator(Extractor testExtractor) {
        this.testExtractor = testExtractor != null
                             ? testExtractor
                             : Extractor.IDENTITY;
    }


    // ---- fluent API ------------------------------------------------------

    /**
     * Set the condition that determines when this validator should be
     * evaluated.
     *
     * @param condition  condition that determines if this validator
     *                   should be evaluated
     *
     * @return validator instance
     */
    public Validator when(String condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.whenCondition = Defaults.createCondition(condition);
        return this;
    }

    /**
     * Set the condition that determines when this validator should be
     * evaluated.
     *
     * @param condition  condition that determines if this validator
     *                   should be evaluated
     *
     * @return this validator instance
     */
    public Validator when(Condition condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.whenCondition = condition;
        return this;
    }

    /**
     * Adds error message for this validator.
     *
     * @param messageId  error message identifier
     * @param providers  providers to add error to if the validator fails
     *
     * @return this validator instance
     */
    public Validator errorMessage(String messageId, String... providers) {
        return action(new ErrorMessageAction(messageId, providers));
    }

    /**
     * Adds an action for this validator.
     *
     * @param action  action that will be executed for this validator
     *
     * @return this validator instance
     */
    public Validator action(ValidationAction action) {
        this.actions.add(action);
        return this;
    }

    // ---- getters and setters ---------------------------------------------

    /**
     * Return the test value extractor.
     *
     * @return test value extractor
     */
    protected Extractor getTestExtractor() {
		return testExtractor;
	}

    /**
     * Return the when condition.
     *
     * @return when condition
     */
	protected Condition getWhenCondition() {
		return whenCondition;
	}

    /**
     * Return validation actions.
     *
     * @return validation actions
     */
    protected List<ValidationAction> getActions() {
        return actions;
    }

    /**
     * Set the validation actions.
     *
     * @param actions  validation actions
     */
    public void setActions(List<ValidationAction> actions) {
    	this.actions = actions;
    }


    // ---- Validator implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean validate(Object validationContext, ValidationErrors errors) {
        boolean valid = true;
        if (whenCondition.evaluate(validationContext)) {
            valid = validate(testExtractor.extract(validationContext));
            processActions(valid, validationContext, errors);
        }

        return valid;
    }


    // ---- helper methods --------------------------------------------------

    /**
     * Validates test object.
     * 
     * @param  objectToValidate object to validate
     *
     * @return <b>true</b> if specified object is valid, <b>false</b> otherwise
     */
    protected abstract boolean validate(Object objectToValidate);

    /**
     * Processes the error messages.
     *
     * @param isValid            whether validator is valid or not
     * @param validationContext  validation context
     * @param errors             validation errors container
     */
    protected void processActions(boolean isValid,
                                  Object validationContext,
                                  ValidationErrors errors) {
        if (actions != null) {
            for (ValidationAction action : actions){
                action.execute(isValid, validationContext, errors);
            }
        }
    }
}