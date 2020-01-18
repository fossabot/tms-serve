package com.odakota.tms.business.auth.job;

import com.odakota.tms.business.auth.repository.AccessTokenRepository;
import com.odakota.tms.enums.auth.Client;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Calendar;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
public class ClearTokenJob extends QuartzJobBean {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        accessTokenRepository.deleteAccessTokenExpired(Client.ADMIN.name(), calendar.getTime());
        calendar.add(Calendar.DATE, -27);
        accessTokenRepository.deleteAccessTokenExpired(Client.CUSTOMER.name(), calendar.getTime());
    }
}
