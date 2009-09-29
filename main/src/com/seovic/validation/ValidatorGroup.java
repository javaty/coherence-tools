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


/**
 * {@link Validator} implementation that supports grouping of validators.
 * <p/>
 * This validator will be valid only when all of the validators withing the
 * group are valid.
 * <p/>
 * Validation errors withing validation group will contain a union of all
 * validation error messages for the contained validators.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public class ValidatorGroup extends BaseValidator {
    // ---- data members ----------------------------------------------------
    
    private List<Validator> validators   = new ArrayList<Validator>();
    private boolean         fastValidate = false;


    // ---- fluent API ------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatorGroup when(String condition) {
        return (ValidatorGroup) super.when(condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatorGroup when(Condition condition) {
        return (ValidatorGroup) super.when(condition);
    }

    /**
     * Adds validator to this group.
     *
     * @param validator  validator instance
     *
     * @return this validator group instance
     */
    public ValidatorGroup add(Validator validator) {
        this.validators.add(validator);
        return this;
    }

    // ---- getters and setters ---------------------------------------------

    /**
     * Return validators.
     *
     * @return  validators
     */
    public List<Validator> getValidators() {
    	return validators;
    }

    /**
     * Set the validators.
     *
     * @param validators the validators
     */
    public void setValidators(List<Validator> validators) {
    	this.validators = validators;
    }

    /**
     * Return the shortcircuit evaluation switch.
     * <p/>
     * When set to <b>true</b>, the validator within the group will only be
     * validated until the first validator fails.
     *
     * @return shortcircuit evaluation switch
     */
    public boolean isFastValidate() {
        return fastValidate;
    }

    /**
     * Set the shortcircuit evaluation switch.
     * <p/>
     * When set to <b>true</b>, the validator within the group will only be
     * validated until the first validator fails.
     *
     * @param fastValidate  shortcircuit evaluation switch
     */
    public void setFastValidate(boolean fastValidate) {
        this.fastValidate = fastValidate;
    }

    // ---- Validator implementation ----------------------------------------

    /**
     * {@inheritDoc}
     */
    public boolean validate(Object validationContext, ValidationErrors errors) {
        boolean valid = true;
        if (getWhenCondition().evaluate(validationContext)) {
            valid = validateGroup(validationContext, errors);
            processActions(valid, validationContext, errors);
        }
        return valid;
    }


    // ---- helper methods --------------------------------------------------

    /**
     * Actual implementation how to validate the specified object.
     *
     * @param validationContext  the object to validate
     * @param errors             validation errors instance to add error
     *                           messages to
     *
     * @return <b>true</b> if validation was successful, <b>false</b> otherwise
     */
    protected boolean validateGroup(Object validationContext, ValidationErrors errors) {
        boolean valid = true;
        for (Validator validator : validators) {
            valid = validator.validate(validationContext, errors) && valid;
            if (!valid && fastValidate) {
                break;
            }
        }
        return valid;
    }

    /**
     * Doesn't do anything for validator group as there is no single test.
     *
     * @param objectToValidate  Object to validate.
     * @return <b>true</b> if specified object is valid, <b>false</b> otherwise.
     */
    @Override
    protected boolean validate(Object objectToValidate) {
    	throw new UnsupportedOperationException("Validator group does not support this method.");
    }
}