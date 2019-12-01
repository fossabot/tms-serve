package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.PermissionRepository;
import com.odakota.tms.business.auth.repository.PermissionRoleRepository;
import com.odakota.tms.business.auth.resource.PermissionResource;
import com.odakota.tms.business.auth.resource.PermissionRoleResource;
import com.odakota.tms.business.auth.resource.PermissionRoleResource.PermissionRoleCondition;
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
public class PermissionRoleService extends BaseService<PermissionRole, PermissionRoleResource, PermissionRoleCondition> {

    private final PermissionRoleRepository permissionRoleRepository;

    private final PermissionRepository permissionRepository;

    private final AuthMapper authMapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public PermissionRoleService(PermissionRoleRepository permissionRoleRepository,
                                 PermissionRepository permissionRepository) {
        super(permissionRoleRepository);
        this.permissionRoleRepository = permissionRoleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Object getPermissionRole(Long roleId) {
        // all permissions ids
        List<Long> ids = new ArrayList<>();
        List<Permission> list = permissionRepository.findByCondition(null);
        for (Permission sysPer : list) {
            ids.add(sysPer.getId());
        }
        List<PermissionResource> treeList = new ArrayList<>();
        getTreeModelList(treeList, list, null);

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("treeList", treeList); // all tree node data
        resMap.put("ids", ids);// all tree ids
        return resMap;
    }

    private void getTreeModelList(List<PermissionResource> treeList, List<Permission> metaList,
                                  PermissionResource temp) {
        for (Permission permission : metaList) {
            Long tempPid = permission.getParentId();
            PermissionResource tree = authMapper.convertToResource(permission);
            if (temp == null && tempPid == null) {
                treeList.add(tree);
                if (!tree.getLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }
        }
    }
}
