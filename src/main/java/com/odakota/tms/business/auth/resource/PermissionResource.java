package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResource extends BaseResource<Permission> {

    private String name;

    private String title;

    private Long parentId;

    private Long key;

    private String url;

    private String component;

    private String componentName;

    private String redirect;

    private Integer menuType;

    private String perms;

    private String permsType;

    private Double sortNo;

    private Boolean alwaysShow;

    private String icon;

    private Boolean route;

    private Boolean leaf;

    private Boolean keepAlive;

    private Boolean hidden;

    private String description;

    private Boolean ruleFlag;

    private Boolean status;

    private Boolean internalOrExternal;

    private List<PermissionResource> children = new ArrayList<>();

    public PermissionResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    public static class PermissionCondition extends BaseCondition {
    }
}
