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
 * Represents exceptions thrown when input data of an operation fail to
 * preconditions.
 * 
 * @author as  2005.11.13
 * @author ic  2009.06.05
 */
public class ValidationException extends RuntimeException {
    // ---- data members ----------------------------------------------------

    private ValidationErrors errors;


    // ---- constructors ----------------------------------------------------

    /**
     * Creates a new instance of the <tt>ValidationException</tt> class.
     */
    public ValidationException() {
    }

    /**
     * Creates a new instance of the <tt>ValidationException</tt> class.
     *
     * @param errors  validation errors
     */
    public ValidationException(ValidationErrors errors) {
        this.errors = errors;
    }

    /**
     * Creates a new instance of the <tt>ValidationException</tt> class.
     *
     * @param message  message about the exception
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of the <tt>ValidationException</tt> class.
     *
     * @param message  message about the exception
     * @param errors   validation errors
     */
    public ValidationException(String message, ValidationErrors errors) {
        super(message);
        this.errors = errors;
    }


    /**
     * Creates a new instance of the <tt>ValidationException</tt> class.
     *
     * @param message    message about the exception
     * @param rootCause  root exception that is being wrapped
     */
    public ValidationException(String message, Exception rootCause) {
        super(message, rootCause);
    }


    /**
     * Creates a new instance of the <tt>ValidationException</tt> class.
     *
     * @param message    message about the exception
     * @param rootCause  root exception that is being wrapped
     * @param errors     validation errors
     */
    public ValidationException(String message, Exception rootCause, ValidationErrors errors) {
        super(message, rootCause);
        this.errors = errors;
    }


    // ---- getters and setters ---------------------------------------------

    /**
     * Return validation errors.
     *
     * @return validation errors
     */
    public ValidationErrors getValidationErrors() {
        return errors;
    }
}
