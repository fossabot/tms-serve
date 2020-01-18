package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.Brand;
import com.odakota.tms.business.auth.resource.BrandResource.BrandCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface BrandRepository extends BaseRepository<Brand, BrandCondition> {

    /**
     * String build = "where b.deletedFlag = false";
     * <p>
     * if(condition.branchName != null){ build = build + "b.branchCode like '%condition.branchName%' or b.branchName
     * like '%condition.branchName%'"; }
     * <p>
     */
    @Query("select b from Brand b where b.deletedFlag = false and (:#{#condition.brandName} is null " +
           "or (b.brandCode like %:#{#condition.brandName}% or b.brandName like %:#{#condition.brandName}%))")
    Page<Brand> findByCondition(@Param("condition") BrandCondition condition, Pageable pageable);
}
