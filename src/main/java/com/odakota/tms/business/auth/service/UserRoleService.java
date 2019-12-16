package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.UserRole;
import com.odakota.tms.business.auth.repository.UserRoleRepository;
import com.odakota.tms.business.auth.resource.UserRoleResource;
import com.odakota.tms.business.auth.resource.UserRoleResource.UserRoleCondition;
import com.odakota.tms.system.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class UserRoleService extends BaseService<UserRole, UserRoleResource, UserRoleCondition> {

    private final UserRoleRepository userRoleRepository;

    protected UserRoleService(UserRoleRepository userRoleRepository) {
        super(userRoleRepository);
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Get list role id by user id
     *
     * @param userId user id
     * @return list permission id
     */
    public List<Long> getUserRoleIds(Long userId) {
        List<UserRole> list = userRoleRepository.findByUserIdAndDeletedFlagFalse(userId);
        return list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }
}
