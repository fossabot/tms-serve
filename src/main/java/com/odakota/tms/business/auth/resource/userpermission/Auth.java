package com.odakota.tms.business.auth.resource.userpermission;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter @Getter
public class Auth implements Serializable {

    private Object action;

    private Object type;

    private String describe;

    private Boolean status;

}
