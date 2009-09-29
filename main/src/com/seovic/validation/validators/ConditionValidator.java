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
 * Evaluates validator test using condition evaluator.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public class ConditionValidator extends BaseValidator {
    // ---- constructors ----------------------------------------------------

    /**
     * Constructs an instance of condition validator class
     */
    public ConditionValidator() {}

    /**
     * Constructs an instance of condition validator class
     *
     * @param test  expression to validate
     */
    public ConditionValidator(String test) {
    	super(test);
    }

    /**
     * Constructs an instance of condition validator class
     *
     * @param test  expression to validate
     */
    public ConditionValidator(Extractor test) {
    	super(test);
	}


    // ---- BaseValidator overrides -----------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
	protected boolean validate(Object objectToValidate){
		return (Boolean) objectToValidate;
	}    
}
