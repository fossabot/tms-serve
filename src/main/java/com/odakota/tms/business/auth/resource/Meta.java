package com.odakota.tms.business.auth.resource;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter @Getter
public class Meta implements Serializable {

    private Boolean keepAlive = true;

    private Boolean internalOrExternal = true;

    private String title;

    private String icon;

    private String url;

    private List<Menu> permissionList;
}
