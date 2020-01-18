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

    /**
     * String build = "where b.deletedFlag = false";
     * <p>
     * if(condition.roleName != null){ build = build + "r.roleName like '%var%' or r.roleCode like '%var%'"; }
     * <p>
     * if(userSession.brandId != null) { build = build + "b.brandId = userSession.brandId"; }
     * <p>
     * else if(userSession.brandId == null && condition.brandId != null){ build = build + "b.brandId =
     * condition.brandId"; }
     * <p>
     * if(userSession.branchId != null) { build = build + "b.branchId = userSession.branchId"; }
     * <p>
     * else if(userSession.branchId == null && condition.branchId != null){ build = build + "b.branchId =
     * condition.branchId"; }
     */
    @Query("select r from Role r where r.deletedFlag = false and (:#{#condition.roleName} is null " +
           "or (r.roleName like %:#{#condition.roleName}% or r.roleCode like %:#{#condition.roleName}%))" +
           "and ((:#{@userSession.brandId} is null and (:#{#condition.brandId} is null " +
           "or r.brandId = :#{#condition.brandId})) or r.brandId = :#{@userSession.brandId})" +
           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
           "or r.branchId = :#{#condition.branchId})) or r.branchId = :#{@userSession.branchId})")
    Page<Role> findByCondition(@Param("condition") RoleCondition condition, Pageable pageable);

    List<Role> findByDeletedFlagFalse();

    long countByBrandIdAndDeletedFlagFalse(Long brandId);

    long countByBranchIdAndDeletedFlagFalse(Long branchId);
}
