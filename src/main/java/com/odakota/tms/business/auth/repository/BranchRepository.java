package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.Branch;
import com.odakota.tms.business.auth.resource.BranchResource.BranchCondition;
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
public interface BranchRepository extends BaseRepository<Branch, BranchCondition> {

    /**
     * String build = "where b.deletedFlag = false";
     * <p>
     * if(condition.branchName != null){ build = build + "b.branchCode like '%condition.branchName%' or b.branchName
     * like '%condition.branchName%'"; }
     * <p>
     * if(userSession.brandId != null) { build = build + "b.brandId = userSession.brandId"; }
     * <p>
     * else if(userSession.brandId == null && condition.brandId != null){ build = build + "b.brandId =
     * condition.brandId"; }
     */
    @Query("select b from Branch b where b.deletedFlag = false and (:#{#condition.branchName} is null " +
           "or (b.branchCode like %:#{#condition.branchName}% or b.branchName like %:#{#condition.branchName}%))" +
           "and ((:#{@userSession.brandId} is null and (:#{#condition.brandId} is null " +
           "or b.brandId = :#{#condition.brandId})) or b.brandId = :#{@userSession.brandId})")
    Page<Branch> findByCondition(@Param("condition") BranchCondition condition, Pageable pageable);

    List<Branch> findByDeletedFlagFalse();

    long countByBrandIdAndDeletedFlagFalse(Long brandId);
}
