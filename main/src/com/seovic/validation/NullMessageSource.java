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


import java.util.Locale;


/**
 * An implementation of {@link MessageSource} that doesn't resolve mesage codes
 * to messages at all. Instead, it simply returns the message code.
 *
 * @author ic  2009.06.08
 */
public class NullMessageSource implements MessageSource {

    public String getMessage(String id) {
        return getMessage(id, Locale.getDefault());
    }

    public String getMessage(String id, Locale locale) {
        return getMessage(id, new Object[0], locale);
    }

    public String getMessage(String id, Object[] args) {
        return getMessage(id, args, Locale.getDefault());
    }

    public String getMessage(String id, Object[] args, Locale locale) {
        return id;
    }
}
