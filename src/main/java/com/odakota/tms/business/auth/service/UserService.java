package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.auth.resource.UserResource.UserCondition;
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

    public User getUser(String username){
        return userRepository.findByUsernameAndDeletedFlagFalse(username)
                             .orElseThrow(() -> new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS,
                                                                    HttpStatus.BAD_REQUEST));
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
        return super.getCondition(condition);
    }
}
