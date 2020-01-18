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
@Table(name = "brand_tbl")
public class Brand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_code", updatable = false)
    private String brandCode;

    @Column(name = "brand_image")
    private String brandImage;
}
