package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.UserRole;
import com.odakota.tms.business.auth.resource.UserRoleResource.UserRoleCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, UserRoleCondition> {

    List<UserRole> findByUserIdAndDeletedFlagFalse(Long userId);
}
