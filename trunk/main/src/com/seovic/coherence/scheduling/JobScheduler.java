package com.seovic.coherence.scheduling;


import com.seovic.coherence.util.processor.MethodInvocationProcessor;

import com.seovic.config.Configuration;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.JobKey;

import java.util.Collection;


/**
 * @author Aleksandar Seovic  2011.04.19
 */
@SuppressWarnings({"unchecked"})
public class JobScheduler {
    /**
     * The name of the job scheduler cache.
     */
    public static final String CACHE_NAME = Configuration.getSchedulerCacheName();

    private static final NamedCache CACHE = CacheFactory.getCache(CACHE_NAME);

    public static void schedule(JobDetail job, Trigger... schedule) {
        CACHE.put(job.getKey(), new ScheduledJob(job, schedule));
    }
    
    public static void unschedule(JobKey key) {
        CACHE.remove(key);
    }

    public static void pause(JobKey key) {
        CACHE.invoke(key, new MethodInvocationProcessor("setPaused", true, true));
    }

    public static void resume(JobKey key) {
        CACHE.invoke(key, new MethodInvocationProcessor("setPaused", true, false));
    }

    public static Collection<ScheduledJob> getJobs() {
        return CACHE.values();
    }
}
