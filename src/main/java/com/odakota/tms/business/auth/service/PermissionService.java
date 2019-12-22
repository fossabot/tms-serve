package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.PermissionRepository;
import com.odakota.tms.business.auth.resource.PermissionResource;
import com.odakota.tms.business.auth.resource.PermissionResource.PermissionCondition;
import com.odakota.tms.system.base.BaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getPermissions() {
        // all permissions ids
        List<Long> ids = new ArrayList<>();
        List<Permission> list = permissionRepository.findByDeletedFlagFalseOrderByIdAscSortNoAsc();
        for (Permission sysPer : list) {
            ids.add(sysPer.getId());
        }
        List<PermissionResource> treeList = new ArrayList<>();
        getTreeList(treeList, list, null);

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("treeList", treeList); // all tree node data
        resMap.put("ids", ids);// all tree ids
        return resMap;
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
                if (temp.getChildren() == null) {
                    temp.setChildren(new ArrayList<>());
                }
                temp.getChildren().add(tree);
                if (!tree.getLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            }
        }
    }
}
