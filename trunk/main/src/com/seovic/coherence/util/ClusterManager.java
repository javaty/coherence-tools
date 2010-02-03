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

package com.seovic.coherence.util;


import com.tangosol.net.Cluster;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.AbstractInvocable;
import com.tangosol.net.InvocationService;

import java.io.Serializable;
import java.util.Set;


/**
 * @author Aleksandar Seovic  2010.02.01
 */
public class ClusterManager
    {
    // ---- constructors ----------------------------------------------------

    public ClusterManager()
        {
        m_cluster = CacheFactory.ensureCluster();
        }

    // ---- main method -----------------------------------------------------

    public static void main(String[] args)
        {
        if (args.length < 1)
            {
            printUsage();
            }

        ClusterManager mgr = new ClusterManager();

        if ("info".equals(args[0].toLowerCase())) mgr.info();
        if ("kill".equals(args[0].toLowerCase())) mgr.kill();

        }

    // ---- command handlers ------------------------------------------------

    private void info()
        {
        System.out.println(m_cluster);
        }

    @SuppressWarnings({"deprecation"})
    private void kill()
        {
        InvocationService is = CacheFactory.getInvocationService("Management");

        Set members = is.getInfo().getServiceMembers();
        members.remove(m_cluster.getLocalMember());

        is.execute(new KillAgent(), members, null);
        }


    // ---- data members ----------------------------------------------------

    private static void printUsage()
        {
        System.out.println("Usage: ClusterManager <command>");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("   info -- print cluster info");
        System.out.println("   kill -- shutdown the cluster");
        }


    // ---- inner class: KillAgent ------------------------------------------

    /**
     * Shuts down a member by invoking System.exit().
     */
    private static class KillAgent
            extends AbstractInvocable
            implements Serializable
        {
        public void run()
            {
            System.exit(0);
            }
        }

    // ---- data members ----------------------------------------------------

    private Cluster m_cluster;
    }
