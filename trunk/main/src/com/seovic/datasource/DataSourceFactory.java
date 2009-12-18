/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.datasource;


import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import javax.sql.DataSource;


/**
 * A factory interface for DataSource objects.
 * <p/>
 * Unlike data sources themselves, DataSourceFactory implementations should be
 * serializable, so they can be passed from one process to another. This
 * allows them to be used within classes that could be executed either locally
 * or remotely, such as batch jobs, for example.
 *
 * @author Ivan Cikic  2009.11.27
 */
public interface DataSourceFactory
    {
    /**
     * Create DataSource instance.
     *
     * @return data source instance
     *
     * @throws Exception  if an error occurs while creating a data source
     */
    public DataSource createDataSource();
    }
