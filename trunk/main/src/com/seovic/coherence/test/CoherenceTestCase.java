package com.seovic.coherence.test;


import com.tangosol.net.*;

import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

import org.junit.Before;


/**
 * A base class that simplifies implementation of JUnit 4.x tests that depend on
 * Coherence.
 *
 * @author Aleksandar Seovic  2008.12.27
 */
@SuppressWarnings("unchecked")
@CoherenceTest(servers = 2)
public class CoherenceTestCase
    {
    private static Cluster cluster;
    private static List<Process> cacheServers = new ArrayList<Process>();

    private String cacheServerMemory;
    private String cacheServerConfig;

    static
        {
        Thread shutdownHook = new Thread()
        {
        @Override
        public void run()
            {
            CoherenceTestCase.stopCacheServers();
            }
        };

        Runtime.getRuntime().addShutdownHook(shutdownHook);
        }

    @Before
    public synchronized void initialize()
        {
        CoherenceTest cfg = getClass().getAnnotation(CoherenceTest.class);

        int cacheServerCount = cfg.servers();
        cacheServerMemory = cfg.memory();
        cacheServerConfig = cfg.config();

        setLocalStorageEnabled(cacheServerCount == 0);

        CacheFactory.setConfigurableCacheFactory(
                createConfigurableCacheFactory(cacheServerConfig));

        if (cluster == null)
            {
            cluster = CacheFactory.ensureCluster();
            }
        ensureCacheServers(cacheServerCount);
        }

    protected boolean isLocalStorageEnabled()
        {
        return Boolean.getBoolean(
                "tangosol.coherence.distributed.localstorage");
        }

    protected void setLocalStorageEnabled(boolean localStorageEnabled)
        {
        System.setProperty(
                "tangosol.coherence.distributed.localstorage",
                Boolean.toString(localStorageEnabled));
        }

    protected ConfigurableCacheFactory createConfigurableCacheFactory(String cacheConfig)
        {
        return new DefaultConfigurableCacheFactory(cacheConfig);
        }

    protected synchronized void ensureCacheServers(int count)
        {

        int diff = count - cacheServers.size();

        if (diff <= 0)
            {
            return;
            }
        if (count == 0)
            {
            stopCacheServers();
            }

        if (diff > 0)
            {
            for (int i = cacheServers.size(); i < count; i++)
                {
                System.out.println("Starting cache server " + (i + 1));
                startCacheServer(i + 1);
                }
            }

        for (int sec = 0;
             cluster.getMemberSet().size() < count + 1 && ++sec < 10;)
            {
            sleep(1000);
            }

        if (cluster.getMemberSet().size() == count + 1)
            {
            sleep(2000); // let the distribution finish
            }
        else
            {
            System.out.println("Failed to start servers: " + cluster);
            }

        }

    private void startCacheServer(int n)
        {
        try
            {
            ProcessBuilder pb = new ProcessBuilder(
                    System.getProperty("java.home") + File.separatorChar + "bin"
                    + File.separatorChar + "java",
                    "-server",
                    "-Xms" + cacheServerMemory,
                    "-Xmx" + cacheServerMemory,
                    "-Dtangosol.coherence.cacheconfig=" + cacheServerConfig,
                    "-cp", System.getProperty("java.class.path"),
                    "com.tangosol.net.DefaultCacheServer");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            new ProcessOutputLogger("CacheServer-" + n, p, new FileOutputStream(
                    "cache-server-" + n + ".log")).start();

            cacheServers.add(p);
            }
        catch (Exception e)
            {
            throw new RuntimeException("Failed to start cache server", e);
            }
        }

    protected static void stopCacheServers()
        {
        for (Process cacheServer : cacheServers)
            {
            try
                {
                InputStream in = cacheServer.getInputStream();
                while (in.available() > 0)
                    {
                    Thread.sleep(1000);
                    }
                }
            catch (Exception ignore)
                {
                }

            cacheServer.destroy();
            }
        cacheServers.clear();
        }

    protected static int getCacheServerCount()
        {
        return cacheServers.size();
        }

    protected static NamedCache getCache(String cacheName)
        {
        return CacheFactory.getCache(cacheName);
        }

    protected static void clearCache(String cacheName)
        {
        CacheFactory.getCache(cacheName).clear();
        }

    protected static void clearAllCaches()
        {
        Cluster cluster = CacheFactory.ensureCluster();
        Enumeration serviceNames = cluster.getServiceNames();

        while (serviceNames.hasMoreElements())
            {
            String serviceName = (String) serviceNames.nextElement();
            Service service = cluster.getService(serviceName);

            if (service instanceof CacheService)
                {
                Enumeration cacheNames =
                        ((CacheService) service).getCacheNames();
                while (cacheNames.hasMoreElements())
                    {
                    clearCache((String) cacheNames.nextElement());
                    }
                }
            }
        }

    protected static void loadCache(String cacheName, String resourcePath)
            throws Exception
        {
        }

    // utility methods

    public static void sleep(long millis)
        {
        if (millis > 0)
            {
            try
                {
                Thread.sleep(millis);
                }
            catch (InterruptedException ignore)
                {
                }
            }
        }

    // ProcessOutputLogger inner class

    public static class ProcessOutputLogger
            extends Thread
        {

        private InputStream in;

        private OutputStream out;

        public ProcessOutputLogger(String name, Process process,
                                   OutputStream stream)
            {
            super("ProcessOutputLogger." + name);
            setDaemon(true);

            in = process.getInputStream();
            out = stream;
            }

        @Override
        public void run()
            {
            while (true)
                {

                try
                    {
                    while (in.available() > 0)
                        {
                        out.write(in.read());
                        }
                    }
                catch (Exception e)
                    {
                    break;
                    }
                }
            }
        }
    }
