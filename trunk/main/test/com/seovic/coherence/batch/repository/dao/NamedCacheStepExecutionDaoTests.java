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


import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.SimpleJobRepository;

import com.seovic.coherence.batch.repository.NamedCacheJobRepositoryFactoryBean;


/**
 * @author Aleksandar Seovic  2009.11.03
 */
public class NamedCacheStepExecutionDaoTests
        extends AbstractStepExecutionDaoTests
    {
    protected StepExecutionDao getStepExecutionDao()
        {
        return new NamedCacheStepExecutionDao();
        }

    protected JobRepository getJobRepository()
        {
        NamedCacheJobRepositoryFactoryBean.clearAll();
        return new SimpleJobRepository(new NamedCacheJobInstanceDao(),
                                       new NamedCacheJobExecutionDao(),
                                       new NamedCacheStepExecutionDao(),
                                       new NamedCacheExecutionContextDao());
        }
    }