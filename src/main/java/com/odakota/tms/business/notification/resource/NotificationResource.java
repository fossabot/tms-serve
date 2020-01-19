package com.odakota.tms.business.notification.resource;

import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResource extends BaseResource<Notification> {

    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    private Date startDate;

    private Date endDate;

    private String sender;

    private Integer priority;

    private Integer category;

    private Integer type;

    private Integer sendStatus;

    private Date sendTime;

    private Date cancelTime;

    public NotificationResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationCondition extends BaseCondition {

        private Date sendTime;

        private Boolean isRead;

        private MsgType type;
    }
}
