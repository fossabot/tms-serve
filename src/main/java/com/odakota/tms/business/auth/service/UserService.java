package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.auth.resource.UserResource.UserCondition;
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
public class UserService extends BaseService<User, UserResource, UserCondition> {

    private final UserRepository userRepository;

    private AuthMapper authMapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        return userRepository.findByUsernameAndDeletedFlagFalse(username)
                             .orElseThrow(() -> new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS,
                                                                    HttpStatus.BAD_REQUEST));
    }

    /**
     * Create a new resource.
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    @Override
    public UserResource createResource(UserResource resource) {
        // check duplicate username
        if (userRepository.isExistedResource(null, FieldConstant.USER_NAME, resource.getUsername())) {
            throw new CustomException(MessageCode.MSG_USER_NAME_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate email
        if (userRepository.isExistedResource(null, FieldConstant.USER_EMAIL, resource.getEmail())) {
            throw new CustomException(MessageCode.MSG_EMAIL_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate phone
        if (userRepository.isExistedResource(null, FieldConstant.USER_PHONE, resource.getPhone())) {
            throw new CustomException(MessageCode.MSG_PHONE_EXISTED, HttpStatus.CONFLICT);
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
    protected UserResource updateResource(Long id, UserResource resource) {
        // check user default
        if (Constant.USER_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_USER_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        // check duplicate email
        if (userRepository.isExistedResource(id, FieldConstant.USER_EMAIL, resource.getEmail())) {
            throw new CustomException(MessageCode.MSG_EMAIL_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate phone
        if (userRepository.isExistedResource(id, FieldConstant.USER_PHONE, resource.getPhone())) {
            throw new CustomException(MessageCode.MSG_PHONE_EXISTED, HttpStatus.CONFLICT);
        }
        return super.updateResource(id, resource);
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        // check user default
        if (Constant.USER_ID_DEFAULT == id) {
            throw new CustomException(MessageCode.MSG_USER_NOT_DELETED, HttpStatus.BAD_REQUEST);
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
    protected UserResource convertToResource(User entity) {
        return authMapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected User convertToEntity(Long id, UserResource resource) {
        User user = authMapper.convertToEntity(resource);
        user.setId(id);
        return user;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected UserCondition getCondition(FindCondition condition) {
        UserCondition userCondition = condition.get(UserCondition.class);
        return userCondition != null ? userCondition : new UserCondition();
    }
}
