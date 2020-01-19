package com.odakota.tms.business.auth.resource;

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
public class ResetPasswordResource {

    private String username;

    private String password;
}
