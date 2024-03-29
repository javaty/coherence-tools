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


import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.seovic.validation.config.ValidationBeanDefinitionParser;


/**
 * Handles validation namespace in a Spring XML configuration file.
 *
 * @author as  2005.11.13
 * @author ic  2009.06.04
 */
public class ValidationNamespaceHandler extends NamespaceHandlerSupport {
    // ----  NamespaceHandler implementation --------------------------------

    /**
     * Registers custom parser used for handling validation elements.
     * @see com.seovic.validation.config.ValidationBeanDefinitionParser
     */
	public void init() {
		registerBeanDefinitionParser("group", new ValidationBeanDefinitionParser());
	}
}