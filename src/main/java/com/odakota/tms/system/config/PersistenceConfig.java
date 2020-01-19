package com.odakota.tms.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.odakota.tms.business")
public class PersistenceConfig {

    @Bean
    AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Class implement for components that are aware of the application's current auditor. This will be some kind of
     * user mostly.
     *
     * @author haidv
     * @version 1.0
     */
    public static class AuditorAwareImpl implements AuditorAware<Long> {

        @Autowired
        private UserSession userSession;

        @Override
        public Optional<Long> getCurrentAuditor() {
            return userSession.getUserId() == null ? Optional.empty() : Optional.of(userSession.getUserId());
        }

    }
}
