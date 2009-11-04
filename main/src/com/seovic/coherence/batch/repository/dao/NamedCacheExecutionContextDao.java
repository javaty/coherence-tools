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

package com.seovic.coherence.batch.repository.dao;


import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;


/**
 * @author Aleksandar Seovic  2009.11.03
 */
@SuppressWarnings({"unchecked"})
public class NamedCacheExecutionContextDao
        implements ExecutionContextDao
    {
    private static NamedCache jobContexts =
            CacheFactory.getCache("spring-batch-job-execution-contexts");

    private static NamedCache stepContexts =
            CacheFactory.getCache("spring-batch-step-execution-contexts");

    public static void clear()
        {
        jobContexts.clear();
        stepContexts.clear();
        }

    public ExecutionContext getExecutionContext(StepExecution stepExecution)
        {
        return (ExecutionContext) stepContexts.get(stepExecution.getId());
        }

    public void updateExecutionContext(StepExecution stepExecution)
        {
        stepContexts.put(stepExecution.getId(), stepExecution.getExecutionContext());
        }

    public ExecutionContext getExecutionContext(JobExecution jobExecution)
        {
        return (ExecutionContext) jobContexts.get(jobExecution.getId());
        }

    public void updateExecutionContext(JobExecution jobExecution)
        {
        jobContexts.put(jobExecution.getId(), jobExecution.getExecutionContext());
        }

    public void saveExecutionContext(JobExecution jobExecution)
        {
        updateExecutionContext(jobExecution);
        }

    public void saveExecutionContext(StepExecution stepExecution)
        {
        updateExecutionContext(stepExecution);
        }
    }
