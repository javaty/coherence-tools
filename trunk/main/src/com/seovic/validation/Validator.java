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
 * Validator represents an object that can validate other objects.
 * <p/>
 * Application developers writing custom {@link Validator} implementations will
 * typically not implement this interface directly. In most cases, custom
 * validators should simply extend the abstract {@link BaseValidator} class,
 * and implement {@link BaseValidator#validate(Object)} method.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public interface Validator {
    /**
     * Validates the specified object.
     *
     * @param validationContext  object to validate
     * @param errors             validation errors instance to add any error
     *                           messages to in the case of validation failure
     *
     * @return <b>true</b> if the validation was successful
     */
    boolean validate(Object validationContext, ValidationErrors errors);
}