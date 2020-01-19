package com.odakota.tms.business.notification.entity;

import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "notification_user_tbl")
public class NotificationUser extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "read_time")
    private Date readTime;
}
