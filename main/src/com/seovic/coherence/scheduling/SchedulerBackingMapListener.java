package com.seovic.coherence.scheduling;


import com.seovic.coherence.util.listener.AbstractBackingMapListener;

import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.MapEvent;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerException;
import org.quartz.JobDetail;
import org.quartz.impl.StdSchedulerFactory;


/**
 * @author Aleksandar Seovic  2011.04.19
 */
public class SchedulerBackingMapListener
        extends AbstractBackingMapListener<JobKey, ScheduledJob> {

    private static Log LOG = LogFactory.getLog(SchedulerBackingMapListener.class);

    private Scheduler scheduler;

    public SchedulerBackingMapListener(BackingMapManagerContext context) {
        super(context);

        SchedulerFactory factory = new StdSchedulerFactory();
        try {
            scheduler = factory.getScheduler();
            scheduler.start();
        }
        catch (SchedulerException e) {
            LOG.error("Failed to initialize Quartz scheduler", e);
        }
    }

    @Override
    public void entryInserted(MapEvent event) {
        ScheduledJob sj = getNewValue(event);
        JobDetail job = sj.getJob();

        try {
            scheduler.scheduleJobs(Collections.singletonMap(job, sj.getTriggers()), false);
            LOG.info("Scheduled job " + job);
        }
        catch (SchedulerException e) {
            LOG.error("Failed to schedule job " + job, e);
        }
    }

    @Override
    public void entryUpdated(MapEvent event) {
        ScheduledJob sj = getNewValue(event);
        JobDetail job = sj.getJob();

        try {
            scheduler.scheduleJobs(Collections.singletonMap(job, sj.getTriggers()), true);
            LOG.info("Rescheduled job " + job);
        }
        catch (SchedulerException e) {
            LOG.error("Failed to reschedule job " + job, e);
        }
    }

    @Override
    public void entryDeleted(MapEvent event) {
        ScheduledJob sj = getOldValue(event);
        JobDetail job = sj.getJob();

        try {
            scheduler.deleteJob(job.getKey());
            LOG.info("Unscheduled job " + job);
        }
        catch (SchedulerException e) {
            LOG.error("Failed to unschedule job " + job, e);
        }
    }
}
