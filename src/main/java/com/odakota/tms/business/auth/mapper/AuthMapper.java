package com.odakota.tms.business.auth.mapper;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.PermissionResource;
import com.odakota.tms.business.auth.resource.PermissionRoleResource;
import com.odakota.tms.business.auth.resource.RoleResource;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.enums.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface AuthMapper {

    RoleResource convertToResource(Role entity);

    Role convertToEntity(RoleResource resource);

    @Mapping(source = "sex", target = "sex", qualifiedByName = "getValueSex")
    UserResource convertToResource(User entity);

    @Mapping(source = "sex", target = "sex", qualifiedByName = "getSexByValue")
    User convertToEntity(UserResource resource);

    @Mappings({@Mapping(source = "name", target = "title", qualifiedByName = "getTitle"),
               @Mapping(source = "id", target = "key", qualifiedByName = "getKey")}
    )
    PermissionResource convertToResource(Permission resource);

    PermissionRole convertToEntity(PermissionRoleResource resource);

    PermissionRoleResource convertToResource(PermissionRole resource);

    @Named("getValueSex")
    default Integer getValueSex(Gender gender) {
        return gender.getValue();
    }

    @Named("getSexByValue")
    default Gender getSexByValue(Integer value) {
        return Gender.of(value);
    }

    @Named("getTitle")
    default String getTitle(String name) {
        return name;
    }

    @Named("getKey")
    default Long getKey(Long id) {
        return id;
    }
}