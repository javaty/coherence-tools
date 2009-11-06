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

package com.seovic.batch;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.seovic.core.DynamicObject;


/**
 * @author Aleksandar Seovic  2009.11.05
 */
public class ExecutionContext
        extends DynamicObject
    {
    /**
     * {@inheritDoc}
     */
    protected Map<String, Object> createPropertyMap()
        {
        return new ConcurrentHashMap<String, Object>();
        }

    public ExecutorService getLocalExecutor()
        {
        if (m_localExecutor == null)
            {
            m_localExecutor = Executors.newCachedThreadPool();
            }
        return m_localExecutor;
        }

    public void setLocalExecutor(ExecutorService localExecutor)
        {
        m_localExecutor = localExecutor;
        }

    public ExecutorService getRemoteExecutor()
        {
        return m_remoteExecutor;
        }

    public void setRemoteExecutor(ExecutorService remoteExecutor)
        {
        m_remoteExecutor = remoteExecutor;
        }


    private transient ExecutorService m_localExecutor;
    private transient ExecutorService m_remoteExecutor;
    }
