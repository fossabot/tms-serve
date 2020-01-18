package com.odakota.tms.business.auth.entity;

import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "role_tbl")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "description")
    private String description;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "is_root")
    private boolean isRoot;
}
