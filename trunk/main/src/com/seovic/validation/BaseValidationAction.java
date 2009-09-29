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


import com.seovic.core.Condition;


/**
 * Abstract base class that should be extended by all validation actions.
 * <p/>
 * This class implements template <tt>execute</tt> method and defines
 * <tt>onValid</tt> and <tt>onInvalid</tt> methods that can be overriden by
 * specific validation actions.
 * 
 * @author as  2005.11.13
 * @author ic  2009.06.04
 */
public abstract class BaseValidationAction implements ValidationAction {
    // ---- data members ----------------------------------------------------

    public Condition whenCondition = Condition.TRUE;


    // ---- constructors ----------------------------------------------------

    /**
     * Initializes a new instance of the <tt>BaseValidationAction</tt> class.
     */
    public BaseValidationAction() {
    }


    // ---- getters and setters ---------------------------------------------

    /**
     * Return the condition that determines if action should be executed.
     *
     * @return condition that determines if action should be executed
     */
    public Condition getWhenCondition() {
		return whenCondition;
	}

    /**
     * Set the condition that determines if action should be executed.
     *
     * @param whenCondition  condition that determines if action should be
     *                       executed
     */
	public void setWhenCondition(Condition whenCondition) {
		this.whenCondition = whenCondition;
	}


    // ---- ValidationAction implementation ---------------------------------

    /**
     * {@inheritDoc}
     */
    public void execute(boolean isValid,
                        Object validationContext,
                        ValidationErrors errors) {
        if (whenCondition.evaluate(validationContext)) {
            if (isValid) {
                onValid(validationContext, errors);
            } else {
                onInvalid(validationContext, errors);
            }
        }
    }


    // ---- helper methods --------------------------------------------------

    /**
     * Called when associated validator is valid.
     *
     * @param validationContext  validation context
     * @param errors             validation errors container
     */
    @SuppressWarnings("UnusedDeclaration")
    protected void onValid(Object validationContext,ValidationErrors errors) {}

     /**
     * Called when associated validator is not valid.
     *
     * @param validationContext  validation context
     * @param errors             validation errors container
     */
    @SuppressWarnings("UnusedDeclaration")
    protected void onInvalid(Object validationContext, ValidationErrors errors) {}
}