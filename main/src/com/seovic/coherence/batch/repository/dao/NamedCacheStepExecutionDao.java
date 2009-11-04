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


import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Entity;
import org.springframework.batch.core.repository.dao.StepExecutionDao;

import org.springframework.util.Assert;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.EqualsFilter;

import com.seovic.coherence.identity.sequence.SequenceGenerator;
import com.seovic.coherence.util.processor.Put;

import com.seovic.core.extractor.ExpressionExtractor;


/**
 * @author Aleksandar Seovic  2009.11.03
 */
@SuppressWarnings({"unchecked"})
public class NamedCacheStepExecutionDao implements StepExecutionDao
    {
    /**
     * Step executions cache.
     */
    private static NamedCache stepExecutions =
            CacheFactory.getCache("spring-batch-step-executions");

    /**
     * Step executions id generator.
     */
    private static SequenceGenerator idGen =
            new SequenceGenerator("SPRING_BATCH_STEP_EXECUTION_ID");

    /**
     * Clear step executions cache.
     */
    public static void clear()
        {
        stepExecutions.clear();
        }

    public void saveStepExecution(StepExecution stepExecution)
        {
        Assert.isTrue(stepExecution.getId() == null);
        Assert.isTrue(stepExecution.getVersion() == null);
        Assert.notNull(stepExecution.getJobExecutionId(), "JobExecution must be saved already.");

        stepExecution.setId(idGen.generateIdentity());
        stepExecution.incrementVersion();
        stepExecutions.invoke(stepExecution.getId(), new Put(stepExecution));
        }

    public void updateStepExecution(StepExecution stepExecution)
        {
        Long id = stepExecution.getId();
        Assert.notNull(stepExecution.getJobExecutionId());
        Assert.isTrue(stepExecutions.containsKey(id),
                      "step execution is expected to be already saved");

        // todo: replace with optimistic locking processor
        stepExecutions.lock(id, -1);
        try
            {
            StepExecution persistedExecution = (StepExecution) stepExecutions.get(id);
            if (!persistedExecution.getVersion().equals(stepExecution.getVersion())) {
                throw new OptimisticLockingFailureException("Attempt to update step execution id=" + id
                        + " with wrong version (" + stepExecution.getVersion() + "), where current version is "
                        + persistedExecution.getVersion());
            }
            stepExecution.incrementVersion();
            stepExecutions.put(stepExecution.getId(), stepExecution);
            }
        finally
            {
            stepExecutions.unlock(id);
            }
        }

    public StepExecution getStepExecution(JobExecution jobExecution,
                                          Long stepExecutionId)
        {
        return (StepExecution) stepExecutions.get(stepExecutionId);
        }

    public void addStepExecutions(JobExecution jobExecution)
        {
        Filter filter = new EqualsFilter(new ExpressionExtractor("jobExecutionId"), jobExecution.getId());
        Set<Map.Entry> entries = stepExecutions.entrySet(filter);

        List<StepExecution> executions = new ArrayList<StepExecution>();
        for (Map.Entry entry : entries)
            {
            executions.add((StepExecution) entry.getValue());
            }
        Collections.sort(executions, new Comparator<Entity>()
            {
            public int compare(Entity o1, Entity o2)
                {
                return Long.signum(o2.getId() - o1.getId());
                }
            });
        jobExecution.addStepExecutions(executions);
        }
    }
