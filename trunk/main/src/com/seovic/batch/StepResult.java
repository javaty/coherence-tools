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


import java.io.Serializable;


/**
 * @author Aleksandar Seovic  2009.11.04
 */
public class StepResult
        implements Serializable
    {
    public StepResult(ExecutionContext context, ExecutionStatus status)
        {
        this(context, status, null);
        }

    public StepResult(ExecutionContext context, ExecutionStatus status, Throwable exception)
        {
        m_context   = context;
        m_status    = status;
        m_exception = exception;
        }

    public ExecutionContext getContext()
        {
        return m_context;
        }

    public ExecutionStatus getStatus()
        {
        return m_status;
        }

    public Throwable getException()
        {
        return m_exception;
        }

    private final ExecutionContext m_context;
    private final ExecutionStatus m_status;
    private final Throwable m_exception;
    }
