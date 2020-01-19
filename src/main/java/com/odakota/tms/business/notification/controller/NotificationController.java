package com.odakota.tms.business.notification.controller;

import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.resource.NotificationResource;
import com.odakota.tms.business.notification.service.NotificationService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.config.data.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class NotificationController extends BaseController<Notification, NotificationResource> {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        super(notificationService);
        this.notificationService = notificationService;
    }

    /**
     * API get notification of current user
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication
    @GetMapping(value = "/notifications", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getNotification(@ModelAttribute @Valid BaseParameter baseReq) {
        return ResponseEntity.ok(new ResponseData<>().success(notificationService.getNotification(baseReq)));
    }
}
