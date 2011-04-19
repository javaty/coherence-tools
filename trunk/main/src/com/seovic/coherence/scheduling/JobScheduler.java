package com.seovic.coherence.scheduling;


import com.seovic.config.Configuration;

import com.tangosol.net.NamedCache;
import com.tangosol.net.CacheFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.JobKey;


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
}
