package com.odakota.tms.system.base;

import com.odakota.tms.system.config.data.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This is the base class for controller classes of applications. <br> The controller provides the function to call
 * various methods of the service through HTTP protocol. This class provides an implementation of the APIs (List, Get,
 * Create, Update, Delete) that should be supported by common CRUD-based controller classes.
 *
 * @param <E> {@link BaseEntity}
 * @param <R> {@link BaseResource}
 * @author haidv
 * @version 1.0
 */
public abstract class BaseController<E extends BaseEntity, R extends BaseResource<E>> {

    private final BaseService<E, R, ?> service;

    protected BaseController(BaseService<E, R, ?> service) {
        this.service = service;
    }

    /**
     * API get resources list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<ResponseData<BaseResponse<R>>> getResources(BaseParameter baseReq) {
        return ResponseEntity.ok(new ResponseData<>().success(service.getResources(baseReq)));
    }

    /**
     * Api get resource
     *
     * @param id Resource identifier (long)
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<ResponseData<R>> getResource(Long id) {
        return ResponseEntity.ok(new ResponseData<>().success(service.getResource(id)));
    }

    /**
     * New resource creation API
     *
     * @param resource resource
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<ResponseData<R>> createResource(@RequestBody R resource) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new ResponseData<>().success(service.createResource(resource)));
    }

    /**
     * Resource update API
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<ResponseData<R>> updateResource(Long id, @RequestBody R resource) {
        return ResponseEntity.ok(new ResponseData<>().success(service.updateResource(id, resource)));
    }

    /**
     * Resource deletion API
     *
     * @param id Resource identifier
     * @return {@link ResponseEntity}
     */
    public ResponseEntity<Void> deleteResource(Long id) {
        service.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}
