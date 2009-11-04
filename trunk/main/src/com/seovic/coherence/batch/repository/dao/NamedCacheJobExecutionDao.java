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


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.repository.dao.JobExecutionDao;

import org.springframework.util.Assert;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import com.tangosol.util.Filter;
import com.tangosol.util.filter.EqualsFilter;

import com.seovic.coherence.identity.sequence.SequenceGenerator;

import com.seovic.coherence.util.processor.Put;
import com.seovic.coherence.util.filter.FilterBuilder;

import com.seovic.core.extractor.ExpressionExtractor;


/**
 * @author Aleksandar Seovic  2009.10.30
 */
@SuppressWarnings({"unchecked"})
public class NamedCacheJobExecutionDao implements JobExecutionDao
    {
    /**
     * Job executions cache.
     */
    private static NamedCache jobExecutions =
            CacheFactory.getCache("spring-batch-job-executions");

    /**
     * Job executions id generator.
     */
    private static SequenceGenerator idGen =
            new SequenceGenerator("SPRING_BATCH_JOB_EXECUTION_ID");

    /**
     * Clear job executions cache.
     */
    public static void clear()
        {
        jobExecutions.clear();
        }

    public void saveJobExecution(JobExecution jobExecution)
        {
        Assert.isTrue(jobExecution.getId() == null);

        jobExecution.setId(idGen.generateIdentity());
        jobExecution.incrementVersion();
        jobExecutions.invoke(jobExecution.getId(), new Put(jobExecution));
        }

    public List<JobExecution> findJobExecutions(JobInstance jobInstance)
        {
        Filter filter = new EqualsFilter(new ExpressionExtractor("jobInstance"), jobInstance);
        Set<Map.Entry> entries = jobExecutions.entrySet(filter);

        List<JobExecution> executions = new ArrayList<JobExecution>();
        for (Map.Entry entry : entries)
            {
            executions.add((JobExecution) entry.getValue());
            }
        Collections.sort(executions, new Comparator<JobExecution>()
            {
            public int compare(JobExecution e1, JobExecution e2)
                {
                long result = (e1.getId() - e2.getId());
                if (result > 0)
                    {
                    return -1;
                    }
                else if (result < 0)
                    {
                    return 1;
                    }
                else
                    {
                    return 0;
                    }
                }
            });
        return executions;
        }

    public void updateJobExecution(JobExecution jobExecution)
        {
        Long id = jobExecution.getId();
        Assert.notNull(id, "JobExecution is expected to have an id (should be saved already)");
        Assert.state(jobExecutions.containsKey(id), "JobExecution must already be saved");

        // todo: replace with optimistic locking processor
        jobExecutions.lock(id, -1);
        try
            {
            JobExecution persistedExecution = (JobExecution) jobExecutions.get(id);
            if (!persistedExecution.getVersion().equals(jobExecution.getVersion())) {
                throw new OptimisticLockingFailureException("Attempt to update job execution id=" + id
                        + " with wrong version (" + jobExecution.getVersion() + "), where current version is "
                        + persistedExecution.getVersion());
            }
            jobExecution.incrementVersion();
            jobExecutions.put(id, jobExecution);
            }
        finally
            {
            jobExecutions.unlock(id);
            }
        }

    public JobExecution getLastJobExecution(JobInstance jobInstance)
        {
        Filter         filter   = new EqualsFilter(new ExpressionExtractor("jobInstance"), jobInstance);
        Set<Map.Entry> entries  = jobExecutions.entrySet(filter);
        JobExecution   lastExec = null;

        for (Map.Entry entry : entries)
            {
            JobExecution exec = (JobExecution) entry.getValue();
            if (lastExec == null)
                {
                lastExec = exec;
                }
            if (lastExec.getCreateTime().before(exec.getCreateTime()))
                {
                lastExec = exec;
                }
            }
        return lastExec;
        }

    public Set<JobExecution> findRunningJobExecutions(String jobName)
        {
        Filter filter = new FilterBuilder()
                .equals("jobInstance.jobName", jobName)
                .equals("running", true)
                .toAnd();
        Set<Map.Entry> entries = jobExecutions.entrySet(filter);

        Set<JobExecution> result = new HashSet<JobExecution>();
        for (Map.Entry entry : entries)
            {
            result.add((JobExecution) entry.getValue());
            }
        return result;
        }

    public JobExecution getJobExecution(Long executionId)
        {
        return (JobExecution) jobExecutions.get(executionId);
        }

    public void synchronizeStatus(JobExecution jobExecution)
        {
        JobExecution saved = getJobExecution(jobExecution.getId());
        if (saved.getVersion().intValue() != jobExecution.getVersion().intValue())
            {
            jobExecution.upgradeStatus(saved.getStatus());
            jobExecution.setVersion(saved.getVersion());
            }
        }
    }
