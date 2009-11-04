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


import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import org.springframework.batch.core.repository.dao.JobInstanceDao;

import com.seovic.coherence.batch.repository.NamedCacheJobRepositoryFactoryBean;


public abstract class AbstractJobInstanceDaoTests
    {
    private static final long DATE = 777;
    protected JobInstanceDao dao = new NamedCacheJobInstanceDao();
    private String fooJob = "foo";
    private JobParameters fooParams = new JobParametersBuilder().addString(
            "stringKey", "stringValue").addLong(
            "longKey", Long.MAX_VALUE)
            .addDouble("doubleKey", Double.MAX_VALUE)
            .addDate("dateKey", new Date(DATE))
            .toJobParameters();

    protected abstract JobInstanceDao getJobInstanceDao();

    @Before
    public void onSetUp()
            throws Exception
        {
        NamedCacheJobRepositoryFactoryBean.clearAll();
        dao = getJobInstanceDao();
        }

    /*
      * Create and retrieve a job instance.
      */
    @Test
    public void testCreateAndRetrieve()
            throws Exception
        {
        JobInstance fooInstance = dao.createJobInstance(fooJob, fooParams);
        assertNotNull(fooInstance.getId());
        assertEquals(fooJob, fooInstance.getJobName());
        assertEquals(fooParams, fooInstance.getJobParameters());

        JobInstance retrievedInstance = dao.getJobInstance(fooJob, fooParams);
        JobParameters retrievedParams = retrievedInstance.getJobParameters();
        assertEquals(fooInstance, retrievedInstance);
        assertEquals(fooJob, retrievedInstance.getJobName());
        assertEquals(fooParams, retrievedParams);

        assertEquals(Long.MAX_VALUE, retrievedParams.getLong("longKey"));
        assertEquals(Double.MAX_VALUE, retrievedParams.getDouble("doubleKey"),
                     0.001);
        assertEquals("stringValue", retrievedParams.getString("stringKey"));
        assertEquals(new Date(DATE), retrievedParams.getDate("dateKey"));
        }

    /*
      * Create and retrieve a job instance.
      */
    @Test
    public void testCreateAndGetById()
            throws Exception
        {
        JobInstance fooInstance = dao.createJobInstance(fooJob, fooParams);
        assertNotNull(fooInstance.getId());
        assertEquals(fooJob, fooInstance.getJobName());
        assertEquals(fooParams, fooInstance.getJobParameters());

        JobInstance retrievedInstance = dao.getJobInstance(fooInstance.getId());
        JobParameters retrievedParams = retrievedInstance.getJobParameters();
        assertEquals(fooInstance, retrievedInstance);
        assertEquals(fooJob, retrievedInstance.getJobName());
        assertEquals(fooParams, retrievedParams);

        assertEquals(Long.MAX_VALUE, retrievedParams.getLong("longKey"));
        assertEquals(Double.MAX_VALUE, retrievedParams.getDouble("doubleKey"),
                     0.001);
        assertEquals("stringValue", retrievedParams.getString("stringKey"));
        assertEquals(new Date(DATE), retrievedParams.getDate("dateKey"));
        }

    /*
      * Create and retrieve a job instance.
      */
    @Test
    public void testGetMissingById()
            throws Exception
        {
        JobInstance retrievedInstance = dao.getJobInstance(1111111L);
        assertNull(retrievedInstance);

        }

    /*
      * Create and retrieve a job instance.
      */
    @Test
    public void testGetJobNames()
            throws Exception
        {
        testCreateAndRetrieve();
        List<String> jobNames = dao.getJobNames();
        assertFalse(jobNames.isEmpty());
        assertTrue(jobNames.contains(fooJob));
        }

    @Test
    public void testGetLastInstances()
            throws Exception
        {
        testCreateAndRetrieve();

        // unrelated job instance that should be ignored by the query
        dao.createJobInstance("anotherJob", new JobParameters());

        // we need two instances of the same job to check ordering
        dao.createJobInstance(fooJob, new JobParameters());

        List<JobInstance> jobInstances = dao.getJobInstances(fooJob, 0, 2);
        assertEquals(2, jobInstances.size());
        assertEquals(fooJob, jobInstances.get(0).getJobName());
        assertEquals(fooJob, jobInstances.get(1).getJobName());
        assertEquals(Integer.valueOf(0), jobInstances.get(0).getVersion());
        assertEquals(Integer.valueOf(0), jobInstances.get(1).getVersion());

        assertTrue("Last instance should be first on the list",
                   jobInstances.get(0).getId() > jobInstances.get(1).getId());
        }

    @Test
    public void testGetLastInstancesPaged()
            throws Exception
        {
        testCreateAndRetrieve();

        // unrelated job instance that should be ignored by the query
        dao.createJobInstance("anotherJob", new JobParameters());

        // we need two instances of the same job to check ordering
        dao.createJobInstance(fooJob, new JobParameters());

        List<JobInstance> jobInstances = dao.getJobInstances(fooJob, 1, 2);
        assertEquals(1, jobInstances.size());
        assertEquals(fooJob, jobInstances.get(0).getJobName());
        assertEquals(Integer.valueOf(0), jobInstances.get(0).getVersion());
        }

    @Test
    public void testGetLastInstancesPastEnd()
            throws Exception
        {
        testCreateAndRetrieve();

        // unrelated job instance that should be ignored by the query
        dao.createJobInstance("anotherJob", new JobParameters());

        // we need two instances of the same job to check ordering
        dao.createJobInstance(fooJob, new JobParameters());

        List<JobInstance> jobInstances = dao.getJobInstances(fooJob, 4, 2);
        assertEquals(0, jobInstances.size());
        }

    /**
     * Trying to create instance twice for the same job+parameters causes error
     */
    @Test
    public void testCreateDuplicateInstance()
        {
        dao.createJobInstance(fooJob, fooParams);

        try
            {
            dao.createJobInstance(fooJob, fooParams);
            fail();
            }
        catch (IllegalStateException e)
            {
            // expected
            }
        }

    @Test
    public void testCreationAddsVersion()
        {
        JobInstance jobInstance = new JobInstance((long) 1, new JobParameters(),
                                                  "testVersionAndId");

        assertNull(jobInstance.getVersion());

        jobInstance = dao.createJobInstance("testVersion", new JobParameters());

        assertNotNull(jobInstance.getVersion());
        }
    }
