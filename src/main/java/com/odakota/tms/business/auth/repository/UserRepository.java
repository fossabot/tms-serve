package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.UserResource.UserCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface UserRepository extends BaseRepository<User, UserCondition> {

    Optional<User> findByUsernameAndDeletedFlagFalse(String username);
}
