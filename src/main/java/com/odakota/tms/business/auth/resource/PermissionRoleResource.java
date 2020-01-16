package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRoleResource extends BaseResource<PermissionRole> {

    private Long roleId;

    private String permissionIds;

    private String lastPermissionIds;

    private String auth;

    /**
     * @author haidv
     * @version 1.0
     */
    public static class PermissionRoleCondition extends BaseCondition {
    }
}
