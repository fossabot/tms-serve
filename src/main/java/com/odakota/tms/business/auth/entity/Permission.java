package com.odakota.tms.business.auth.entity;

import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "permission_tbl")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "permission_name")
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "url")
    private String url;

    @Column(name = "component")
    private String component;

    @Column(name = "component_name")
    private String componentName;

    @Column(name = "redirect")
    private String redirect;

    @Column(name = "menu_type")
    private Integer menuType;

    @Column(name = "perms")
    private String perms;

    @Column(name = "perms_type")
    private String permsType;

    @Column(name = "sort_no")
    private Double sortNo;

    @Column(name = "always_show")
    private Boolean alwaysShow;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_route")
    private Boolean route;

    @Column(name = "is_leaf")
    private Boolean leaf;

    @Column(name = "keep_alive")
    private Boolean keepAlive;

    @Column(name = "hidden")
    private Boolean hidden;

    @Column(name = "description")
    private String description;

    @Column(name = "rule_flag")
    private Boolean ruleFlag;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "internal_or_external")
    private Boolean internalOrExternal;
}
