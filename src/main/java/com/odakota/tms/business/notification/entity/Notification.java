package com.odakota.tms.business.notification.entity;

import com.odakota.tms.business.notification.mapper.convert.MsgTypeConverter;
import com.odakota.tms.business.notification.mapper.convert.PriorityConverter;
import com.odakota.tms.business.notification.mapper.convert.SendStatusConverter;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "notification_tbl")
public class Notification extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "sender")
    private String sender;

    @Column(name = "priority")
    @Convert(converter = PriorityConverter.class)
    private Priority priority;

    @Column(name = "type")
    @Convert(converter = MsgTypeConverter.class)
    private MsgType type;

    @Column(name = "send_status")
    @Convert(converter = SendStatusConverter.class)
    private SendStatus sendStatus;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "cancel_time")
    private Date cancelTime;
}
