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


import org.springframework.batch.core.explore.support.AbstractJobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheJobInstanceDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheJobExecutionDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheStepExecutionDao;
import com.seovic.coherence.batch.repository.dao.NamedCacheExecutionContextDao;


/**
 * @author Aleksandar Seovic  2009.11.03
 */
public class NamedCacheJobExplorerFactoryBean
        extends AbstractJobExplorerFactoryBean
    {
    protected JobInstanceDao createJobInstanceDao()
            throws Exception
        {
        return new NamedCacheJobInstanceDao();
        }

    protected JobExecutionDao createJobExecutionDao()
            throws Exception
        {
        return new NamedCacheJobExecutionDao();
        }

    protected StepExecutionDao createStepExecutionDao()
            throws Exception
        {
        return new NamedCacheStepExecutionDao();
        }

    protected ExecutionContextDao createExecutionContextDao()
            throws Exception
        {
        return new NamedCacheExecutionContextDao();
        }

    public Object getObject()
            throws Exception
        {
        return new SimpleJobExplorer(createJobInstanceDao(),
                                     createJobExecutionDao(),
                                     createStepExecutionDao(),
                                     createExecutionContextDao());
        }
    }
