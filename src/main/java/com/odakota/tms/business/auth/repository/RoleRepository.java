package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.resource.RoleResource.RoleCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, RoleCondition> {

    @Query("select r from Role r " +
           "where r.deletedFlag = false and (:#{#condition.roleName} is null " +
           "or (r.roleName like %:#{#condition.roleName}% or r.roleCode like %:#{#condition.roleName}%))")
    Page<Role> findByCondition(@Param("condition") RoleCondition condition, Pageable pageable);

    List<Role> findByDeletedFlagFalse();
}
