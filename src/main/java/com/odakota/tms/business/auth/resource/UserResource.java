package com.odakota.tms.business.auth.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResource extends BaseResource<User> {

    private String username;

    private String password;

    private String fullName;

    private int sex;

    @DateTimeFormat(pattern = Constant.YYYY_MM_DD)
    @JsonFormat(pattern = Constant.YYYY_MM_DD)
    private Date birthDay;

    private String avatar;

    private String email;

    private String phone;

    private boolean disableFlag;

    public UserResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCondition extends BaseCondition {

        private String username;

        private String email;

        private String phone;

        private Boolean disableFlag;
    }
}
