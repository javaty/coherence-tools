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

package com.seovic.validation.config;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanEntry;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.seovic.validation.ValidatorGroup;
import com.seovic.validation.actions.ErrorMessageAction;
import com.seovic.validation.validators.ConditionValidator;
import com.seovic.validation.validators.RequiredValidator;


/**
 *  TODO: write javadoc
 *
 * @author ic  2009.06.05
 */                                                                                   
public class ValidationBeanDefinitionParser implements/*extends AbstractSingle*/BeanDefinitionParser {

	private ParseState parseState = new ParseState();
    // TODO: is there a need for this
	//private int definitionCount;

    /**
     * Parse the validation element and register the resulting bean definitions
     *
     * @param element        element that is to be parsed into one or more
     *                       <tt>BeanDefinitions</tt>
     * @param parserContext  the object encapsulating the current state of the
     *                       parsing process
     * 
     * @return the primary <tt>BeanDefinition</tt>
     */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		if (!element.hasAttribute("id")) {
			throw new IllegalStateException("Top-level validator element must have an 'id' attribute defined.");
        }
        return parseAndRegisterValidator(element, parserContext);
	}

	private RuntimeBeanReference parseAndRegisterValidator2(Element element, ParserContext parserContext) {
		String         id        = element.getAttribute("id");
		BeanDefinition validator = parseValidator(element, parserContext);
        if (StringUtils.hasText(id)) {
        	parserContext.getRegistry().registerBeanDefinition(id, validator);
        } else {
        	id = parserContext.getReaderContext().registerWithGeneratedName(validator);
        }
        return new RuntimeBeanReference(id);
    }

	private BeanDefinition parseAndRegisterValidator(Element element, ParserContext parserContext) {
		try {
		    String id = element.getAttribute("id");
		    this.parseState.push(new BeanEntry(id));
		    BeanDefinition validator = parseValidator(element, parserContext);
            if (StringUtils.hasText(id)) {
        	    parserContext.getRegistry().registerBeanDefinition(id, validator);
            }
//            } else {
//        	    @SuppressWarnings("unused")
//			    String beanName = parserContext.getReaderContext().registerWithGeneratedName(validator);
//            }
            return validator;
		} finally {
			this.parseState.pop();
		}
    }

	@SuppressWarnings("unchecked")
	private BeanDefinition parseValidator(Element element, ParserContext parserContext) {
		String parent = element.getAttribute("parent");
		if ("".equals(parent)) {
            parent = null;
        }
        String test                  = element.getAttribute("test");
        String when                  = element.getAttribute("when");
        String validateAll           = element.getAttribute("validate-all");
        String context               = element.getAttribute("context");
        String includeElementsErrors = element.getAttribute("include-element-errors");

        MutablePropertyValues properties = new MutablePropertyValues();
        if(StringUtils.hasText(test)) {
        	properties.addPropertyValue("test", test);
        }
        if(StringUtils.hasText(when)) {
        	properties.addPropertyValue("when", when);
        }
        if(StringUtils.hasText(validateAll)) {
        	properties.addPropertyValue("validateAll", validateAll);
        }
        if(StringUtils.hasText(context)) {
        	properties.addPropertyValue("context", context);
        }
        if(StringUtils.hasText(includeElementsErrors)) {
        	properties.addPropertyValue("includeElementsErrors", includeElementsErrors);
        }
        ManagedList nestedValidators = new ManagedList();
        ManagedList actions          = new ManagedList();
        for (int i  = 0; i < element.getChildNodes().getLength(); i++) {
            Node child = element.getChildNodes().item(i);
            if (child != null && child instanceof Element) {
            	Element childElement = (Element)child;
            	if ("message".equals(childElement.getLocalName())) {
            		actions.add(parseErrorMessageAction(childElement, parserContext));
            	} else {
            		nestedValidators.add(parseAndRegisterValidator2(childElement, parserContext));
            	}
            }
        }
        if (nestedValidators.size() > 0) {
            properties.addPropertyValue("validators", nestedValidators);
        }
        if (actions.size() > 0) {
            properties.addPropertyValue("actions", actions);
        }
        String className;
        if ("required".equals(element.getLocalName())) {
        	className = RequiredValidator.class.getName();
        } else if ("condition".equals(element.getLocalName())) {
        	className = ConditionValidator.class.getName();
        } else if ("validator".equals(element.getLocalName())) {
        	className = element.getAttribute("type");
        }else {
        	className = ValidatorGroup.class.getName();
        }
        AbstractBeanDefinition validatorDefinition;
		try {
			validatorDefinition = BeanDefinitionReaderUtils.createBeanDefinition(
					parent, className, parserContext.getReaderContext().getBeanClassLoader());
		} catch (ClassNotFoundException e) {
            throw new BeanCreationException("Error occured during creation of bean definition", e);
		}
		validatorDefinition.setResource(parserContext.getReaderContext().getResource());
        validatorDefinition.setSource(parserContext.extractSource(element));
        validatorDefinition.setPropertyValues(properties);
        validatorDefinition.setLazyInit(true);
        validatorDefinition.setScope("singleton");
        return validatorDefinition;
    }

	private static BeanDefinition parseErrorMessageAction(Element message, ParserContext parserContext) {
        String   messageId = message.getAttribute("id");
        String[] providers = message.getAttribute("providers").split(",");
        String   typeName = ErrorMessageAction.class.getName();

        ConstructorArgumentValues ctorArgs = new ConstructorArgumentValues();
        ctorArgs.addGenericArgumentValue(messageId);
        ctorArgs.addGenericArgumentValue(providers);

        String when = message.getAttribute("when");
        MutablePropertyValues properties = new MutablePropertyValues();
        if (StringUtils.hasText(when)) {
            properties.addPropertyValue("when", when);
        }
        AbstractBeanDefinition action;
		try {
			action = BeanDefinitionReaderUtils.createBeanDefinition(
					null, typeName, parserContext.getReaderContext().getBeanClassLoader());
		} catch (ClassNotFoundException e) {
			throw new BeanCreationException("Error occured during creation of bean definition", e);
		}
        action.setConstructorArgumentValues(ctorArgs);
        action.setPropertyValues(properties);
        return action;
    }
}