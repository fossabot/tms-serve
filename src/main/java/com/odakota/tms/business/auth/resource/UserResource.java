package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

        private String userName;
    }
}
