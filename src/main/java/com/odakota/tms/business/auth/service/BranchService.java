package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Branch;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.BranchRepository;
import com.odakota.tms.business.auth.resource.BranchResource;
import com.odakota.tms.business.auth.resource.BranchResource.BranchCondition;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class BranchService extends BaseService<Branch, BranchResource, BranchCondition> {

    private final BranchRepository branchRepository;

    private AuthMapper mapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public BranchService(BranchRepository branchRepository) {
        super(branchRepository);
        this.branchRepository = branchRepository;
    }


    /**
     * Create a new branch.
     *
     * @param resource resource
     * @return The created branch is returned.
     */
    @Override
    public BranchResource createResource(BranchResource resource) {
        // check duplicate branchCode
        if (branchRepository.isExistedResource(null, FieldConstant.BRANCH_CODE, resource.getBranchCode())) {
            throw new CustomException(MessageCode.MSG_BRANCH_CODE_EXISTED, HttpStatus.CONFLICT);
        }
        return super.createResource(resource);
    }

    /**
     * Update resources.
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return The updated resource is returned.
     */
    @Override
    protected BranchResource updateResource(Long id, BranchResource resource) {
        // check branch default
        if (Constant.BRANCH_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_BRANCH_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        return super.updateResource(id, resource);
    }

    /**
     * Delete branch by id.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        // check branch default
        if (Constant.BRANCH_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_BRANCH_NOT_DELETED, HttpStatus.BAD_REQUEST);
        }
        super.deleteResource(id);
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected BranchResource convertToResource(Branch entity) {
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
    protected Branch convertToEntity(Long id, BranchResource resource) {
        Branch entity = mapper.convertToEntity(resource);
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
    protected BranchCondition getCondition(FindCondition condition) {
        BranchCondition roleCondition = condition.get(BranchCondition.class);
        return roleCondition != null ? roleCondition : new BranchCondition();
    }
}
