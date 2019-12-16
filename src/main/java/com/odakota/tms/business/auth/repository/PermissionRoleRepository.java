package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.resource.PermissionRoleResource.PermissionRoleCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PermissionRoleRepository extends BaseRepository<PermissionRole, PermissionRoleCondition> {

    List<PermissionRole> findByRoleIdAndDeletedFlagFalse(Long roleId);

    Optional<PermissionRole> findByRoleIdAndPermissionIdAndDeletedFlagFalse(Long roleId, Long permissionId);
}
