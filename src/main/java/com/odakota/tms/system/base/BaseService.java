package com.odakota.tms.system.base;

import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is the super interface for the service class of applications. <br> The service provides methods for read / write
 * operations on multiple entities using resource classes. <br> This interface defines the most common methods that
 * should be supported in all service classes.
 *
 * @param <E> {@link BaseEntity}
 * @param <R> {@link BaseResource}
 * @author haidv
 * @version 1.0
 */
public abstract class BaseService<E extends BaseEntity, R extends BaseResource<E>, C extends BaseCondition> {

    private final BaseRepository<E, C> repository;

    protected BaseService(BaseRepository<E, C> repository) {
        this.repository = repository;
    }

    /**
     * Get a list of resources.
     *
     * @param baseReq request param
     * @return Resource search results are returned.
     */
    public BaseResponse<R> getResources(BaseParameter baseReq) {
        C condition = this.getCondition(baseReq.getFindCondition());
        Pageable pageRequest = BaseParameter.getPageable(baseReq.getSort(), baseReq.getPage(), baseReq.getLimit());
        if (pageRequest == null) {
            return new BaseResponse<>(this.getResources(repository.findByCondition(condition)));
        }
        Page<E> page = repository.findByCondition(condition, pageRequest);
        return new BaseResponse<>(this.getResources(page.getContent()), page);
    }

    /**
     * Specify a resource identifier to get one resource.
     *
     * @param id Resource identifier
     * @return Resource search results are returned in {@link Optional}.
     */
    public R getResource(Long id) {
        return repository.findByIdAndDeletedFlagFalse(id)
                         .map(this::convertToResource)
                         .orElseThrow(() -> new CustomException(MessageCode.MSG_RESOURCE_NOT_EXIST,
                                                                HttpStatus.NOT_FOUND));
    }

    /**
     * Create a new resource.
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    public R createResource(R resource) {
        E entity = this.convertToEntity(resource.getId(), resource);
        repository.save(entity);
        resource.setId(entity.getId());
        return resource;
    }

    /**
     * Update resources.
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return The updated resource is returned.
     */
    protected R updateResource(Long id, R resource) {
        if (!isExistedResource(id)) {
            throw new CustomException(MessageCode.MSG_RESOURCE_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
        E entity = convertToEntity(id, resource);
        repository.save(entity);
        resource.setId(id);
        return resource;
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param id Resource identifier
     */
    public void deleteResource(Long id) {
        repository.deleteById(id);
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param ids list resource identifier
     */
    @Transactional
    public void deleteResource(List<Long> ids) {
        ids.forEach(this::deleteResource);
    }

    /**
     * Delete the resource.
     *
     * @param e Resource
     */
    public void deleteResource(E e) {
        repository.delete(e);
    }

    /**
     * Checks whether the data store contains elements with the given entity identifier.
     *
     * @param id Resource identifier
     * @return boolean
     */
    public boolean isExistedResource(Long id) {
        return repository.existsByIdAndDeletedFlagFalse(id);
    }

    /**
     * Convert list entity to list resource
     *
     * @param list list entity
     * @return list resource
     */
    protected List<R> getResources(List<E> list) {
        return list.stream().map(this::convertToResource).collect(Collectors.toList());
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    protected R convertToResource(E entity) {
        return null;
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    protected E convertToEntity(Long id, R resource) {
        return null;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    protected C getCondition(FindCondition condition) {
        return null;
    }
}
