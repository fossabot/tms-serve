package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.resource.PermissionResource.PermissionCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission, PermissionCondition> {

    @Query("select p from Permission p where exists (select pr from PermissionRole pr join Role r on r.id = pr.roleId "
           + "join UserRole ur on r.id = ur.roleId where p.id = pr.permissionId and ur.userId = ?1) "
           + "and p.deletedFlag = false order by p.sortNo asc ")
    List<Permission> findByUserId(Long userId);

    List<Permission> findByDeletedFlagFalseAndMenuType(Integer menuType);

    List<Permission> findByDeletedFlagFalseOrderBySortNoAsc();
}
