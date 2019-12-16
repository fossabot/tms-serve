package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResource extends BaseResource<Role> {

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    @Size(max = 256, message = MessageCode.MSG_MAX_LENGTH)
    private String roleName;

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    @Size(max = 256, message = MessageCode.MSG_MAX_LENGTH)
    private String roleCode;

    @Size(max = 2000, message = MessageCode.MSG_MAX_LENGTH)
    private String description;

    public RoleResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleCondition extends BaseCondition {

        private String roleName;
    }
}
