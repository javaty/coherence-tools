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


import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;


/**
 * An {@link DataSourceFactory} implementation that uses Spring's
 * <b>DriverManagerDataSource</b>.
 *
 * @author Ivan Cikic  2009.11.27
 */
public class DriverManagerDataSourceFactory
        implements DataSourceFactory, Serializable
    {
    public DriverManagerDataSourceFactory(String url, String username, String password)
        {
        m_url      = url;
        m_username = username;
        m_password = password;
        }

    public DataSource createDataSource()
        {
            return new DriverManagerDataSource(m_url, m_username, m_password);
        }

    private String m_url;
    private String m_username;
    private String m_password;

    }

