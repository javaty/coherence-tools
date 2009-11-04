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


import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;

import org.springframework.batch.item.ExecutionContext;

import com.seovic.coherence.batch.repository.NamedCacheJobRepositoryFactoryBean;


/**
 * Tests for {@link ExecutionContextDao} implementations.
 */
public abstract class AbstractExecutionContextDaoTests
    {
    private ExecutionContextDao contextDao;
    private JobExecution jobExecution;
    private StepExecution stepExecution;

    @Before
    public void setUp()
        {
        NamedCacheJobRepositoryFactoryBean.clearAll();

        JobInstanceDao jobInstanceDao = getJobInstanceDao();
        JobExecutionDao jobExecutionDao = getJobExecutionDao();
        StepExecutionDao stepExecutionDao = getStepExecutionDao();
        contextDao = getExecutionContextDao();

        JobInstance ji = jobInstanceDao.createJobInstance("testJob", new JobParameters());
        jobExecution = new JobExecution(ji);
        jobExecutionDao.saveJobExecution(jobExecution);
        stepExecution = new StepExecution("stepName", jobExecution);
        stepExecutionDao.saveStepExecution(stepExecution);
        }

    /**
     * @return Configured {@link ExecutionContextDao} implementation ready for
     *         use.
     */
    protected abstract JobExecutionDao getJobExecutionDao();

    /**
     * @return Configured {@link ExecutionContextDao} implementation ready for
     *         use.
     */
    protected abstract JobInstanceDao getJobInstanceDao();

    /**
     * @return Configured {@link ExecutionContextDao} implementation ready for
     *         use.
     */
    protected abstract StepExecutionDao getStepExecutionDao();

    /**
     * @return Configured {@link ExecutionContextDao} implementation ready for
     *         use.
     */
    protected abstract ExecutionContextDao getExecutionContextDao();

    @Test
    public void testSaveAndFindJobContext()
        {
        ExecutionContext ctx = new ExecutionContext(
                Collections.<String, Object>singletonMap("key", "value"));
        jobExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(jobExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(jobExecution);
        assertEquals(ctx, retrieved);
        }

    @Test
    public void testSaveAndFindEmptyJobContext()
        {
        ExecutionContext ctx = new ExecutionContext();
        jobExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(jobExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(jobExecution);
        assertEquals(ctx, retrieved);
        }

    @Test
    public void testUpdateContext()
        {
        ExecutionContext ctx = new ExecutionContext(
                Collections.<String, Object>singletonMap("key", "value"));
        jobExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(jobExecution);

        ctx.putLong("longKey", 7);
        contextDao.updateExecutionContext(jobExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(jobExecution);
        assertEquals(ctx, retrieved);
        assertEquals(7, retrieved.getLong("longKey"));
        }

    @Test
    public void testSaveAndFindStepContext()
        {

        ExecutionContext ctx = new ExecutionContext(
                Collections.<String, Object>singletonMap("key", "value"));
        stepExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(stepExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(stepExecution);
        assertEquals(ctx, retrieved);
        }

    @Test
    public void testSaveAndFindEmptyStepContext()
        {

        ExecutionContext ctx = new ExecutionContext();
        stepExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(stepExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(stepExecution);
        assertEquals(ctx, retrieved);
        }

    @Test
    public void testUpdateStepContext()
        {

        ExecutionContext ctx = new ExecutionContext(
                Collections.<String, Object>singletonMap("key", "value"));
        stepExecution.setExecutionContext(ctx);
        contextDao.saveExecutionContext(stepExecution);

        ctx.putLong("longKey", 7);
        contextDao.updateExecutionContext(stepExecution);

        ExecutionContext retrieved = contextDao.getExecutionContext(stepExecution);
        assertEquals(ctx, retrieved);
        assertEquals(7, retrieved.getLong("longKey"));
        }

    @Test
    public void testStoreInteger()
        {

        ExecutionContext ec = new ExecutionContext();
        ec.put("intValue", 343232);
        stepExecution.setExecutionContext(ec);
        contextDao.saveExecutionContext(stepExecution);
        ExecutionContext restoredEc = contextDao.getExecutionContext(stepExecution);
        assertEquals(ec, restoredEc);
	}

}
