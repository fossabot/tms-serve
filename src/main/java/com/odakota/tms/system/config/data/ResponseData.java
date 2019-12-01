package com.odakota.tms.system.config.data;

import com.odakota.tms.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private String timestamp;

    private String message;

    private T body;

    public ResponseData() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.DD_MM_YYYY_HH_MM_SS));
        this.success = true;
        this.message = "Successful!";
    }

    public ResponseData success(T body) {
        this.body = body;
        return this;
    }

    public ResponseData error() {
        this.success = false;
        this.message = "Errors appear!";
        return this;
    }

    public ResponseData error(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public ResponseData error(String message, T body) {
        this.body = body;
        this.success = false;
        this.message = message;
        return this;
    }
}
