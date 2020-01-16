package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.repository.PermissionRepository;
import com.odakota.tms.business.auth.repository.PermissionRoleRepository;
import com.odakota.tms.business.auth.resource.PermissionRoleResource;
import com.odakota.tms.business.auth.resource.PermissionRoleResource.PermissionRoleCondition;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class PermissionRoleService extends BaseService<PermissionRole, PermissionRoleResource, PermissionRoleCondition> {

    private final PermissionRoleRepository permissionRoleRepository;

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionRoleService(PermissionRoleRepository permissionRoleRepository,
                                 PermissionRepository permissionRepository) {
        super(permissionRoleRepository);
        this.permissionRoleRepository = permissionRoleRepository;
        this.permissionRepository = permissionRepository;
    }

    /**
     * Get list permission id by role id
     *
     * @param roleId role id
     * @return list permission id
     */
    public List<Long> getPermissionRoleIds(Long roleId) {
        List<PermissionRole> list = permissionRoleRepository.findByRoleIdAndDeletedFlagFalse(roleId);
        return list.stream().map(PermissionRole::getPermissionId).collect(Collectors.toList());
    }

    /**
     * Save permission role changes
     *
     * @param resource PermissionRoleResource
     */
    @Transactional
    public void saveRolePermissions(PermissionRoleResource resource) {
        String[] preIds = resource.getLastPermissionIds().split(",");
        String[] newIds = resource.getPermissionIds().split(",");
        // get list permission id will add new
        List<Long> addIds = Arrays.stream(newIds).filter(tmp -> !Arrays.asList(preIds).contains(tmp))
                                  .map(Long::parseLong).collect(Collectors.toList());
        // get list permission id will delete
        List<Long> deleteIds = Arrays.stream(preIds).filter(tmp -> !Arrays.asList(newIds).contains(tmp))
                                     .map(Long::parseLong).collect(Collectors.toList());
        // add permission role
        addIds.forEach(tmp -> {
            Permission permission = permissionRepository.findByIdAndDeletedFlagFalse(tmp)
                                                        .orElseThrow(() -> new CustomException(
                                                                MessageCode.MSG_RESOURCE_NOT_EXIST,
                                                                HttpStatus.BAD_REQUEST));
            PermissionRole permissionRole = new PermissionRole();
            permissionRole.setRoleId(resource.getRoleId());
            permissionRole.setPermissionId(tmp);
            permissionRole.setApiId(permission.getPerms());
            permissionRoleRepository.save(permissionRole);
        });
        // delete permission role
        deleteIds.forEach(tmp -> permissionRoleRepository
                .findByRoleIdAndPermissionIdAndDeletedFlagFalse(resource.getRoleId(), tmp)
                .ifPresent(permissionRoleRepository::delete));
    }
}
