package com.odakota.tms.system.config.exception;

import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.config.data.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

/**
 * REST API common error handler class. <br> From the exception class thrown without being caught by the controller, the
 * error code / HTTP status code to be returned to the client is judged, converted into a common HTTP error response
 * format, and sent to the client. <br>
 *
 * @author haidv
 * @version 1.0
 */
@Slf4j
@EnableWebMvc
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Customize the response for MissingServletRequestParameterException. Triggered when a 'required' request parameter
     * is missing.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        log.error("Trace MissingServletRequestParameterException: ", ex);
        return buildResponseHandler(HttpStatus.BAD_REQUEST,
                                    String.format(getMessage(MessageCode.MSG_REQUEST_PARAM_MISTING),
                                                  ex.getParameterName()));
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        log.error("Trace HttpMediaTypeNotSupportedException: ", ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseHandler(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2));
    }

    /**
     * Customize the response for HttpRequestMethodNotSupportedException.
     * <p>This method logs a warning, sets the "Allow" header, and delegates to
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        ThreadContext.put(FieldConstant.REQUEST_ID, request.getHeader(FieldConstant.REQUEST_ID));
        log.error("Trace HttpRequestMethodNotSupportedException: ", ex);
        return buildResponseHandler(HttpStatus.METHOD_NOT_ALLOWED, getMessage(MessageCode.MSG_METHOD_NOT_SUPPORT));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        log.error("Trace MethodArgumentNotValidException: ", ex);
        return buildResponseHandler(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Customize the response for HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        log.error("Trace HttpMessageNotReadableException: ", ex);
        return buildResponseHandler(HttpStatus.BAD_REQUEST, getMessage(MessageCode.MSG_JSON_MALFORMED));
    }

    /**
     * Customize the response for HttpMessageNotWritableException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        log.error("Trace HttpMessageNotWritableException: ", ex);
        return buildResponseHandler(HttpStatus.INTERNAL_SERVER_ERROR, getMessage(MessageCode.MSG_JSON_WRITING_ERROR));
    }

    /**
     * Customize the response for NoHandlerFoundException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
        ThreadContext.put(FieldConstant.REQUEST_ID, request.getHeader(FieldConstant.REQUEST_ID));
        log.error("Trace NoHandlerFoundException: ", ex);
        return buildResponseHandler(HttpStatus.NOT_FOUND, String.format(getMessage(MessageCode.MSG_NOT_FOUND_URL),
                                                                        ex.getHttpMethod(), ex.getRequestURL()));
    }

    /**
     * Customize the response for HttpMediaTypeNotAcceptableException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status,
                                                                      WebRequest request) {
        ThreadContext.put(FieldConstant.REQUEST_ID, request.getHeader(FieldConstant.REQUEST_ID));
        log.error("Trace HttpMediaTypeNotAcceptableException: ", ex);
        return buildResponseHandler(HttpStatus.NOT_ACCEPTABLE, getMessage(MessageCode.MSG_NOT_ACCEPTABLE));
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        log.error("Trace EntityNotFoundException: ", ex);
        return buildResponseHandler(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Trace DataIntegrityViolationException: ", ex);
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseHandler(HttpStatus.CONFLICT, getMessage(MessageCode.MSG_DATABASE_ERROR));
        }
        return buildResponseHandler(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Trace MethodArgumentTypeMismatchException: ", ex);
        return buildResponseHandler(HttpStatus.BAD_REQUEST,
                                    String.format(getMessage(MessageCode.MSG_REQ_PARAM_NOT_MATCH_TYPE),
                                                  ex.getName(), ex.getValue(),
                                                  Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
    }

    /**
     * Handles UnAuthorizedException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException ex) {
        log.error("Trace UnAuthorizedException: ", ex);
        return buildResponseHandler(HttpStatus.UNAUTHORIZED, getMessage(ex.getMessage()));
    }

    /**
     * Handles CustomRuntimeException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(CustomException ex) {
        log.error("Trace CustomException: ", ex);
        String code = ex.getMessage() == null ? MessageCode.MSG_RUNTIME_EXCEPTION : ex.getMessage();
        return buildResponseHandler(ex.getStatus(), getMessage(code));
    }

    /**
     * Handles RuntimeException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        log.error("Trace RuntimeException: ", ex);
        return buildResponseHandler(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Build response
     *
     * @param httpStatus HttpStatus
     * @param message    message
     * @return a {@code ResponseEntity} instance
     */
    private ResponseEntity<Object> buildResponseHandler(HttpStatus httpStatus, String message) {
        ThreadContext.remove(FieldConstant.REQUEST_ID);
        ThreadContext.remove(FieldConstant.START_TIME_KEY);
        return ResponseEntity.status(httpStatus).body(new ResponseData<>().error(message));
    }

    /**
     * Get message by message code
     *
     * @param code message code
     * @return message
     */
    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}