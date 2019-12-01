package com.odakota.tms.business.auth.resource;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter @Getter
public class Example implements Serializable {

    private List<Menu> menu = null;

    private List<Auth> auth = null;

    private List<Auth> allAuth = null;

}
