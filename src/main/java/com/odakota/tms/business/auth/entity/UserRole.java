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
@Table(name = "user_role_tbl")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "role_id")
    private Long userId;

    @Column(name = "user_id")
    private Long roleId;
}
