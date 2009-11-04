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


import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.util.Assert;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import com.tangosol.util.Filter;
import com.tangosol.util.aggregator.DistinctValues;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.EqualsFilter;

import com.seovic.coherence.identity.sequence.SequenceGenerator;
import com.seovic.coherence.util.filter.FilterBuilder;
import com.seovic.coherence.util.processor.Put;

import com.seovic.core.extractor.ExpressionExtractor;


/**
 * @author Aleksandar Seovic  2009.10.30
 */
@SuppressWarnings({"unchecked"})
public class NamedCacheJobInstanceDao implements JobInstanceDao
    {
    /**
     * Job instances cache.
     */
    private static NamedCache jobInstances =
            CacheFactory.getCache("spring-batch-job-instances");

    /**
     * Job instance id generator.
     */
    private static SequenceGenerator idGen =
            new SequenceGenerator("SPRING_BATCH_JOB_INSTANCE_ID");

    /**
     * Clear job instances cache.
     */
    public static void clear()
        {
        jobInstances.clear();
        }

    /**
     * {@inheritDoc}
     */
    public JobInstance createJobInstance(String jobName, JobParameters jobParameters)
        {
        Assert.state(getJobInstance(jobName, jobParameters) == null,
                     "JobInstance must not already exist");

        JobInstance jobInstance = new JobInstance(idGen.generateIdentity(),
                                                  jobParameters,
                                                  jobName);
        jobInstance.incrementVersion();
        jobInstances.invoke(jobInstance.getId(), new Put(jobInstance));

        return jobInstance;
        }

    /**
     * {@inheritDoc}
     */
    public JobInstance getJobInstance(String jobName, JobParameters jobParameters)
        {
        Filter filter = new FilterBuilder()
                .equals("jobName", jobName)
                .equals("jobParameters", jobParameters)
                .toAnd();

        Set<Map.Entry> results = jobInstances.entrySet(filter);
        if (results.isEmpty())
            {
            return null;
            }

        Map.Entry result = results.iterator().next();
        return (JobInstance) result.getValue();
        }

    /**
     * {@inheritDoc}
     */
    public JobInstance getJobInstance(Long instanceId)
        {
        return (JobInstance) jobInstances.get(instanceId);
        }

    /**
     * {@inheritDoc}
     */
    public List<String> getJobNames()
        {
        Collection<String> result = (Collection<String>) jobInstances.aggregate(
                AlwaysFilter.INSTANCE, new DistinctValues(new ExpressionExtractor("jobName")));
        List<String> names = new ArrayList<String>(result);
        Collections.sort(names);
        return names;
        }

    /**
     * {@inheritDoc}
     */
    public List<JobInstance> getJobInstances(String jobName, int start, int count)
        {
        Filter filter = new EqualsFilter(new ExpressionExtractor("jobName"), jobName);
        Set<Map.Entry> entries = jobInstances.entrySet(filter);

        List<JobInstance> result = new ArrayList<JobInstance>();
        for (Map.Entry entry : entries)
            {
            result.add((JobInstance) entry.getValue());
            }
        Collections.sort(result, new Comparator<JobInstance>()
            {
            // sort by ID descending
            public int compare(JobInstance o1, JobInstance o2)
                {
                return Long.signum(o2.getId() - o1.getId());
                }
            });
        if (start >= result.size())
            {
            start = result.size();
            }
        if (start + count >= result.size())
            {
            count = result.size();
            }
        return result.subList(start, count);
        }

    /**
     * {@inheritDoc}
     */
    public JobInstance getJobInstance(JobExecution jobExecution)
        {
        return jobExecution.getJobInstance();
        }
    }
