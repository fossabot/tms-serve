package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.UserResource.UserCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface UserRepository extends BaseRepository<User, UserCondition> {

    @Query("select e from User e where e.deletedFlag = false " +
           "and (:#{#condition.email} is null or e.email like %:#{#condition.email}%) " +
           "and (:#{#condition.phone} is null or e.phone like %:#{#condition.phone}%) " +
           "and (:#{#condition.disableFlag} is null or e.disableFlag = :#{#condition.disableFlag}) " +
           "and (:#{#condition.username} is null or e.username like %:#{#condition.username}% or e.fullName like %:#{#condition.username}%)")
    Page<User> findByCondition(UserCondition condition, Pageable pageable);

    Optional<User> findByUsernameAndDeletedFlagFalse(String username);

    Optional<User> findByUsernameOrPhoneAndDeletedFlagFalse(String username, String phone);

    List<User> findByDeletedFlagFalse();

    long countByBrandIdAndDeletedFlagFalse(Long brandId);

    long countByBranchIdAndDeletedFlagFalse(Long branchId);
}
