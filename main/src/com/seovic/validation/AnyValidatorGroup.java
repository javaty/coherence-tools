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


/**
 * {@link Validator} implementation that supports grouping of validators.
 * <p/>
 * This validator will be valid when <b>one or more</b> of the validators in the
 * underlying collection are valid.
 * <p/>
 * <tt>validationErrors</tt> property will return a union of all validation
 * error messages for the contained validators, but only if this validator is
 * not valid (meaning, when none of the contained validators are valid).
 *
 * @author as  2005.11.13
 * @author ic  2009.06.08
 */
public class AnyValidatorGroup extends ValidatorGroup {
    // ---- constructors ----------------------------------------------------

    /**
     * Initializes a new instance of the <tt>AnyValidatorGroup</tt> class.
     */
    public AnyValidatorGroup() {
        setFastValidate(true);
    }


    // ---- ValidatorGroup overrides ----------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean validateGroup(Object validationContext, ValidationErrors errors) {
        DefaultValidationErrors tmpErrors = new DefaultValidationErrors();
        boolean valid = false;
        for (Validator validator : getValidators()) {
            valid = validator.validate(validationContext, tmpErrors) || valid;
            if (valid && isFastValidate()) {
                break;
            }
        }
        if (!valid) {
            errors.mergeErrors(tmpErrors);
        }
        return valid;
    }
}
