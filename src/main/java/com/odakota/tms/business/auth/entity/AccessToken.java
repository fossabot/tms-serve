package com.odakota.tms.business.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "access_token_tbl")
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jti")
    private String jti;

    @Column(name = "refresh_jti")
    private String refreshJti;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "client")
    private String client;

    @Column(name = "created_date")
    private Date createDate;
}
