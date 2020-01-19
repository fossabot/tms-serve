package com.odakota.tms.business.notification.service;

import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.mapper.NotificationMapper;
import com.odakota.tms.business.notification.repository.NotificationRepository;
import com.odakota.tms.business.notification.resource.NotificationResource;
import com.odakota.tms.business.notification.resource.NotificationResource.NotificationCondition;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class NotificationService extends BaseService<Notification, NotificationResource, NotificationCondition> {

    private final NotificationRepository notificationRepository;

    private NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        super(notificationRepository);
        this.notificationRepository = notificationRepository;
    }

    /**
     * Get notification
     *
     * @param baseReq {@link BaseParameter}
     * @return Object
     */
    public Object getNotification(BaseParameter baseReq) {
        Map<String, Object> map = new HashMap<>();
        NotificationCondition condition = this.getCondition(baseReq.getFindCondition());
        Pageable pageRequest = BaseParameter.getPageable(baseReq.getSort(), baseReq.getPage(), baseReq.getLimit());
        // get notification system
        condition.setType(MsgType.SYSTEM);
        Page<Notification> notify = notificationRepository.findByCondition(condition, pageRequest);
        map.put("notify", new BaseResponse<>(this.getResources(notify.getContent()), notify));
        // get message
        condition.setType(MsgType.NOTIFICATION_BULLETIN);
        Page<Notification> msg = notificationRepository.findByCondition(condition, pageRequest);
        map.put("msg", new BaseResponse<>(this.getResources(msg.getContent()), msg));
        return map;
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected NotificationResource convertToResource(Notification entity) {
        return mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected Notification convertToEntity(Long id, NotificationResource resource) {
        Notification entity = mapper.convertToEntity(resource);
        entity.setId(id);
        return entity;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected NotificationCondition getCondition(BaseParameter.FindCondition condition) {
        NotificationCondition notificationCondition = condition.get(NotificationCondition.class);
        return notificationCondition != null ? notificationCondition : new NotificationCondition();
    }
}
