package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.PermissionRepository;
import com.odakota.tms.business.auth.repository.PermissionRoleRepository;
import com.odakota.tms.business.auth.resource.PermissionRoleResource;
import com.odakota.tms.business.auth.service.PermissionRoleService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.config.data.ResponseData;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequestMapping("permission-roles")
public class PermissionRoleController extends BaseController<PermissionRole, PermissionRoleResource> {

    private final PermissionRoleService permissionRoleService;

    private final PermissionRoleRepository permissionRoleRepository;

    private final PermissionRepository permissionRepository;

    private final AuthMapper authMapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public PermissionRoleController(PermissionRoleService permissionRoleService,
                                    PermissionRoleRepository permissionRoleRepository,
                                    PermissionRepository permissionRepository) {
        super(permissionRoleService);
        this.permissionRoleService = permissionRoleService;
        this.permissionRoleRepository = permissionRoleRepository;
        this.permissionRepository = permissionRepository;
    }


    @GetMapping(produces = ApiVersion.API_VERSION_1)
    @RequiredAuthentication(value = ApiId.R_ROLE)
    public ResponseEntity<ResponseData<?>> getPermissionRole() {
        return ResponseEntity.ok(new ResponseData<>().success(permissionRoleService.getPermissionRole((long) 1)));
    }

    @NoAuthentication
    @RequestMapping(value = "/queryRolePermission", method = RequestMethod.GET)
    public ResponseEntity<ResponseData<?>> queryRolePermission(
            @RequestParam(name = "roleId", required = false) String roleId) {
        List<PermissionRole> list = permissionRoleRepository.findByRoleIdAndDeletedFlagFalse((long) 1);
        return ResponseEntity.ok(new ResponseData<>()
                                         .success(list.stream().map(tmp -> tmp.getPermissionId())
                                                      .collect(Collectors.toList())));
    }

    @NoAuthentication
    @RequestMapping(value = "/getSystemMenuList", method = RequestMethod.GET)
    public ResponseEntity<ResponseData<?>> getSystemMenuList() {
        return ResponseEntity.ok(new ResponseData().success(
                permissionRepository.findByDeletedFlagFalseAndMenuType(0)
                                    .stream().map(tmp -> authMapper.convertToResource(tmp))
                                    .collect(Collectors.toList())));
    }
}
