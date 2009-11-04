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

package com.seovic.coherence.batch.repository;


import org.springframework.batch.core.repository.support.SimpleJobRepository;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.FactoryBean;

import com.seovic.coherence.batch.repository.dao.NamedCacheJobInstanceDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheJobExecutionDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheStepExecutionDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheExecutionContextDao;


/**
 * @author Aleksandar Seovic  2009.10.30
 */
public class NamedCacheJobRepositoryFactoryBean
        implements FactoryBean<JobRepository>
    {
    public static void clearAll()
        {
        NamedCacheJobInstanceDao.clear();
        NamedCacheJobExecutionDao.clear();
        NamedCacheStepExecutionDao.clear();
        NamedCacheExecutionContextDao.clear();
        }

    public Class<JobRepository> getObjectType()
        {
        return JobRepository.class;
        }

    public boolean isSingleton()
        {
        return true;
        }

    public JobRepository getObject()
            throws Exception
        {
        return new SimpleJobRepository(new NamedCacheJobInstanceDao(),
                                       new NamedCacheJobExecutionDao(),
                                       new NamedCacheStepExecutionDao(),
                                       new NamedCacheExecutionContextDao());
        }
    }
