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

package com.seovic.coherence.util;


import com.seovic.core.Command;

import com.tangosol.util.InvocableMap;


/**
 * A {@link Command} adapter for entry processors.
 *
 * @author Aleksandar Seovic  2010.02.01
 */
public class EntryProcessorCommand<T>
        implements Command<T>
    {
    public EntryProcessorCommand(InvocableMap targetMap, Object targetKey,
                                 InvocableMap.EntryProcessor processor)
        {
        m_targetMap = targetMap;
        m_targetKey = targetKey;
        m_processor = processor;
        }

    @SuppressWarnings({"unchecked"})
    public T execute()
        {
        return (T) m_targetMap.invoke(m_targetKey, m_processor);
        }

    private final InvocableMap m_targetMap;
    private final Object m_targetKey;
    private final InvocableMap.EntryProcessor m_processor;
    }


