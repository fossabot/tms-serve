package com.odakota.tms.business.auth.resource.userpermission;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter @Getter
public class Menu implements Serializable {

    private Long id;

    private Integer route;

    private String path;

    private String name;

    private Boolean hidden = true;

    private Boolean alwaysShow = true;

    private String component;

    private PermissionMetaResource meta;

    private Object redirect;

    private List<Menu> children = null;
}
