package com.odakota.tms.business.files.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@AllArgsConstructor
public class UploadResource {

    private String path;

    private String name;
}
