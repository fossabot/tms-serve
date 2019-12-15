package com.odakota.tms.system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Getter @Setter
public class UserSession {

    private Long userId;

    private String username;
}
