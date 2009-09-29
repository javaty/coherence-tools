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
 * An action that should be executed after validator is evaluated.
 * <p/>
 * This interface allows us to define the actions that should be executed
 * after validation in a generic fashion.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.04
 */
public interface ValidationAction {

    /**
     * Executes the action.
     *
     * @param isValid            wheter associated validator is valid or not
     * @param validationContext  validation context
     * @param errors             validation errors container
     */
	void execute(boolean isValid,
                 Object  validationContext,
                 ValidationErrors errors);
}