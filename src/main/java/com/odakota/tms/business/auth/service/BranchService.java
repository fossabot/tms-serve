package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Branch;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.BranchRepository;
import com.odakota.tms.business.auth.repository.BrandRepository;
import com.odakota.tms.business.auth.repository.RoleRepository;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.BranchResource;
import com.odakota.tms.business.auth.resource.BranchResource.BranchCondition;
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

    private final BrandRepository brandRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private AuthMapper mapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public BranchService(BranchRepository branchRepository,
                         BrandRepository brandRepository,
                         RoleRepository roleRepository,
                         UserRepository userRepository) {
        super(branchRepository);
        this.branchRepository = branchRepository;
        this.brandRepository = brandRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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
        // check brand existed
        if (!brandRepository.existsByIdAndDeletedFlagFalse(resource.getBrandId())) {
            throw new CustomException(MessageCode.MSG_BRAND_NOT_EXISTED, HttpStatus.CONFLICT);
        }
        return super.createResource(resource);
    }

    /**
     * Delete branch by id.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        // check branch existed in elsewhere
        if (roleRepository.countByBranchIdAndDeletedFlagFalse(id) > 0 ||
            userRepository.countByBranchIdAndDeletedFlagFalse(id) > 0) {
            throw new CustomException(MessageCode.MSG_BRANCH_NOT_DELETED, HttpStatus.CONFLICT);
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
        BranchCondition branchCondition = condition.get(BranchCondition.class);
        return branchCondition != null ? branchCondition : new BranchCondition();
    }
}
