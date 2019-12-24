package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    boolean existsByJti(String jti);

    @Modifying
    @Transactional
    @Query("delete from AccessToken where jti = :jti")
    void deleteAccessTokenByJti(@Param("jti") String jti);

}
