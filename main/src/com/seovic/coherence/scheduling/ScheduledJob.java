package com.seovic.coherence.scheduling;


import java.io.Serializable;

import java.util.List;
import java.util.Arrays;

import org.quartz.JobDetail;
import org.quartz.Trigger;


/**
 * @author Aleksandar Seovic  2011.04.19
*/
public class ScheduledJob implements Serializable {
    private JobDetail     job;
    private List<Trigger> triggers;
    private boolean       paused;

    public ScheduledJob(JobDetail job, Trigger... schedule) {
        this(job, Arrays.asList(schedule));
    }

    public ScheduledJob(JobDetail job, List<Trigger> triggers) {
        this.job      = job;
        this.triggers = triggers;
    }

    public JobDetail getJob() {
        return job;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
