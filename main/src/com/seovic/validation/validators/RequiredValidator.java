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

package com.seovic.validation.validators;


import com.seovic.validation.BaseValidator;

import com.seovic.core.Extractor;


/**
 * Validates that required value is not empty.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public class RequiredValidator extends BaseValidator {
    // ---- constructors ----------------------------------------------------

    /**
     * Constructs an instance of required validator class
     */
    public RequiredValidator() {}

    /**
     * Constructs an instance of required validator class
     *
     * @param test  expression to validate
     */
    public RequiredValidator(String test) {
    	super(test);
        if (test == null || test.length() == 0) {
            throw new IllegalArgumentException(
                    "Expression used for validation cannot be null or empty");
        }
    }

    /**
     * Constructs an instance of required validator class
     *
     * @param test  expression to validate
     */
    public RequiredValidator(Extractor test) {
    	super(test);
        if (test == null) {
            throw new IllegalArgumentException(
                    "Extractor used for validation cannot be null or empty");
        }
    }


    // ---- BaseValidator overrides -----------------------------------------

    /**
     * {@inheritDoc}
     */
    protected boolean validate(Object objectToValidate) {
        return objectToValidate != null
                && !(objectToValidate instanceof String
                     && "".equals(((String) objectToValidate).trim()));
    }
}