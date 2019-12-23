package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.Branch;
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
public class BranchResource extends BaseResource<Branch> {

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    @Size(max = 128, message = MessageCode.MSG_MAX_LENGTH)
    private String branchName;

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    @Size(max = 16, message = MessageCode.MSG_MAX_LENGTH)
    private String branchCode;

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    @Size(max = 500, message = MessageCode.MSG_MAX_LENGTH)
    private String branchAddress;

    public BranchResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchCondition extends BaseCondition {

        private String branchName;
    }
}
