package com.odakota.tms.system.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Base class for all business exceptions thrown by the application.
 *
 * @author haidv
 * @version 1.0
 */
public class CustomException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Constructs a new exception object with the specified error code and message code.
     *
     * @param message message code
     * @param status  HttpStatus
     */
    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Constructs a new exception object with the specified error code, message code, and cause. <br> The detail message
     * associated with cause is not automatically integrated into this exception's detail message.
     *
     * @param message message code
     * @param cause   cause
     * @param status  HttpStatus
     */
    CustomException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    /**
     * Returns the HttpStatus for this exception object.
     *
     * @return An error code is returned.
     */
    public HttpStatus getStatus() {
        return status;
    }
}
