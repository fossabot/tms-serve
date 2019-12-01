package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.UserRole;
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
public class UserRoleResource extends BaseResource<UserRole> {

    private Long roleId;

    private Long userId;

    public UserRoleResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    public static class UserRoleCondition extends BaseCondition {
    }
}
