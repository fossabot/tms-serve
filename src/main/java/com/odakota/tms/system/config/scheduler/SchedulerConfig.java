package com.odakota.tms.system.config.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
public class SchedulerConfig {

    private final DataSource dataSource;

    private final ApplicationContext context;

    private final QuartzProperties quartzProperties;

    @Autowired
    public SchedulerConfig(DataSource dataSource, ApplicationContext context,
                           QuartzProperties quartzProperties) {
        this.dataSource = dataSource;
        this.context = context;
        this.quartzProperties = quartzProperties;
    }

    /**
     * create scheduler
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {

        SchedulerJobFactory jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(context);
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setOverwriteExistingJobs(true);
        factoryBean.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        factoryBean.setQuartzProperties(properties);
        factoryBean.setJobFactory(jobFactory);
        return factoryBean;
    }
}
