package com.odakota.tms.business.auth.mapper;

import com.odakota.tms.business.auth.entity.*;
import com.odakota.tms.business.auth.resource.*;
import com.odakota.tms.enums.auth.Gender;
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

    BranchResource convertToResource(Branch entity);

    Branch convertToEntity(BranchResource resource);

    BrandResource convertToResource(Brand entity);

    Brand convertToEntity(BrandResource resource);

    @Mapping(source = "sex", target = "sex", qualifiedByName = "getValueSex")
    UserResource convertToResource(User entity);

    @Mapping(source = "sex", target = "sex", qualifiedByName = "getSexByValue")
    User convertToEntity(UserResource resource);

    @Mappings({@Mapping(source = "name", target = "title", qualifiedByName = "getTitle"),
               @Mapping(source = "id", target = "key", qualifiedByName = "getKey")}
    )
    PermissionResource convertToResource(Permission resource);

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
