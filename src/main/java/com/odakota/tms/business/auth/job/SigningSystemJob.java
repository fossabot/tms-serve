package com.odakota.tms.business.auth.job;

import com.odakota.tms.system.service.scheduler.JobScheduleCreator;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
public class SigningSystemJob {

    private final JobScheduleCreator jobScheduleCreator;

    private final ApplicationContext applicationContext;

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final static String JOB_NAME = "clearTokenJob";
    private final static String TRIGGER_NAME = "clearTokenTrigger";
    private final static String GROUP = "system";
    private final static String CRON_EXPRESSION = "0 0 0 1,15 * ?";

    @Autowired
    public SigningSystemJob(JobScheduleCreator jobScheduleCreator,
                            ApplicationContext applicationContext,
                            SchedulerFactoryBean schedulerFactoryBean) {
        this.jobScheduleCreator = jobScheduleCreator;
        this.applicationContext = applicationContext;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Bean(name = JOB_NAME)
    public void clearTokenScheduler() throws SchedulerException {
        JobDetail jobDetail = jobScheduleCreator.createJob(ClearTokenJob.class, true,
                                                           applicationContext, JOB_NAME, GROUP, null);
        CronTrigger cronTrigger = jobScheduleCreator.createCronTrigger(TRIGGER_NAME, GROUP, new Date(), CRON_EXPRESSION,
                                                                       SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        // delete job when existed
        if (scheduler.checkExists(jobDetail.getKey())){
            scheduler.deleteJob(jobDetail.getKey());
        }
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}
