package com.odakota.tms.system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Getter @Setter
public class UserSession {

    private Long userId;

    private List<Long> roleIds;

    private List<Long> test;

    private String tokenId;

    private Long brandId;

    private Long branchId;
}
