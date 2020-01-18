package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Brand;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.BranchRepository;
import com.odakota.tms.business.auth.repository.BrandRepository;
import com.odakota.tms.business.auth.repository.RoleRepository;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.BrandResource;
import com.odakota.tms.business.auth.resource.BrandResource.BrandCondition;
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
public class BrandService extends BaseService<Brand, BrandResource, BrandCondition> {

    private final BrandRepository brandRepository;

    private final BranchRepository branchRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private AuthMapper mapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public BrandService(BrandRepository brandRepository,
                        BranchRepository branchRepository,
                        RoleRepository roleRepository,
                        UserRepository userRepository) {
        super(brandRepository);
        this.brandRepository = brandRepository;
        this.branchRepository = branchRepository;
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
    public BrandResource createResource(BrandResource resource) {
        // check duplicate brandCode
        if (brandRepository.isExistedResource(null, FieldConstant.BRANCH_CODE, resource.getBrandCode())) {
            throw new CustomException(MessageCode.MSG_BRANCH_CODE_EXISTED, HttpStatus.CONFLICT);
        }
        return super.createResource(resource);
    }

    /**
     * Delete brand by id.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        // check brand exist in elsewhere
        if (branchRepository.countByBrandIdAndDeletedFlagFalse(id) > 0 ||
            roleRepository.countByBrandIdAndDeletedFlagFalse(id) > 0 ||
            userRepository.countByBrandIdAndDeletedFlagFalse(id) > 0) {
            throw new CustomException(MessageCode.MSG_BRAND_NOT_DELETED, HttpStatus.CONFLICT);
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
    protected BrandResource convertToResource(Brand entity) {
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
    protected Brand convertToEntity(Long id, BrandResource resource) {
        Brand entity = mapper.convertToEntity(resource);
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
    protected BrandCondition getCondition(FindCondition condition) {
        BrandCondition brandCondition = condition.get(BrandCondition.class);
        return brandCondition != null ? brandCondition : new BrandCondition();
    }
}
