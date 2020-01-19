package com.odakota.tms.business.notification.mapper;

import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.resource.NotificationResource;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface NotificationMapper {

    @Mappings({@Mapping(source = "priority", target = "priority", qualifiedByName = "getValuePriority"),
               @Mapping(source = "type", target = "type", qualifiedByName = "getValueMsgType"),
               @Mapping(source = "sendStatus", target = "sendStatus", qualifiedByName = "getValueSendStatus")}
    )
    NotificationResource convertToResource(Notification entity);

    @Mappings({@Mapping(source = "priority", target = "priority", qualifiedByName = "getPriorityByValue"),
               @Mapping(source = "type", target = "type", qualifiedByName = "getMsgTypeByValue"),
               @Mapping(source = "sendStatus", target = "sendStatus", qualifiedByName = "getSendStatusByValue")}
    )
    Notification convertToEntity(NotificationResource resource);

    @Named("getValuePriority")
    default Integer getValuePriority(Priority priority) {
        return priority == null ? null : priority.getValue();
    }

    @Named("getPriorityByValue")
    default Priority getPriorityByValue(Integer value) {
        return Priority.of(value);
    }

    @Named("getValueSendStatus")
    default Integer getValueSendStatus(SendStatus sendStatus) {
        return sendStatus == null ? null : sendStatus.getValue();
    }

    @Named("getSendStatusByValue")
    default SendStatus getSendStatusByValue(Integer value) {
        return SendStatus.of(value);
    }

    @Named("getValueMsgType")
    default Integer getValueMsgType(MsgType msgType) {
        return msgType == null ? null : msgType.getValue();
    }

    @Named("getMsgTypeByValue")
    default MsgType getMsgTypeByValue(Integer value) {
        return MsgType.of(value);
    }
}
