package com.odakota.tms.business.notification.repository;

import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.resource.NotificationResource.NotificationCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface NotificationRepository extends BaseRepository<Notification, NotificationCondition> {

    @Query("select n from Notification n join NotificationUser nu on n.id = nu.notificationId where n.deletedFlag = false " +
           "and nu.userId = :#{@userSession.userId} and (:#{#condition.isRead} is null or nu.isRead = :#{#condition.isRead}) " +
           "and n.type = :#{#condition.type}")
    Page<Notification> findByCondition(NotificationCondition condition, Pageable pageable);
}
