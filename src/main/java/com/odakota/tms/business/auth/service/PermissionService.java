package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.PermissionRepository;
import com.odakota.tms.business.auth.resource.PermissionResource;
import com.odakota.tms.business.auth.resource.PermissionResource.PermissionCondition;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class PermissionService extends BaseService<Permission, PermissionResource, PermissionCondition> {

    private final PermissionRepository permissionRepository;

    private final AuthMapper authMapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getResources(Long userId) {
        return permissionRepository.findByUserId(userId);
    }

    public List<Permission> getResources(Integer menuType) {
        return permissionRepository.findByDeletedFlagFalseAndMenuType(menuType);
    }

    public BaseResponse<PermissionResource> getPermissions() {
        List<PermissionResource> treeList = new ArrayList<>();
        getTreeList(treeList, permissionRepository.findByDeletedFlagFalseOrderBySortNoAsc(), null);
        return new BaseResponse<>(treeList);
    }

    private void getTreeList(List<PermissionResource> treeList, List<Permission> metaList, PermissionResource temp) {
        for (Permission permission : metaList) {
            Long tempPid = permission.getParentId();
            PermissionResource tree = authMapper.convertToResource(permission);
            if (temp == null && tempPid == null) {
                treeList.add(tree);
                if (!tree.getLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                temp.getChildren().add(tree);
                if (!tree.getLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            }
        }
    }
}
