package com.wisetechglobal.exercise.controller.exception;


import com.wisetechglobal.exercise.controller.response.BaseResponse;
import com.wisetechglobal.exercise.utilities.ConstantsAware;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.stream.Collectors;

/**
 * Global exception handler.
 * This ensures that whatever exception handling is missed in our logic is caught here
 * So that server error information does not land of the clint response
 */
@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements ConstantsAware {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error("inside controller advice of IllegalArgumentException & IllegalStateException", ex);
        String message = "Invalid Input";
        BaseResponse<Object> bodyOfResponse = new BaseResponse<>(400, message);
        HttpHeaders responseHeaders = getResponseHeaders();
        return handleExceptionInternal(ex, bodyOfResponse, responseHeaders, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccessException(RuntimeException ex, WebRequest request) {
        log.error("Database is down ", ex);
        BaseResponse<Object> bodyOfResponse = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), MessageFormat.format(PROCESSING_ERROR, ex.getLocalizedMessage()));
        HttpHeaders responseHeaders = getResponseHeaders();
        return handleExceptionInternal(ex, bodyOfResponse, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        log.error("Not Found Exception", ex);
        BaseResponse<Object> bodyOfResponse = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), ERROR_RECORD_NOT_FOUND);
        HttpHeaders responseHeaders = getResponseHeaders();
        return handleExceptionInternal(ex, bodyOfResponse, responseHeaders, HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex, HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        log.error("inside controller advice of HttpMessageNotReadableException", ex);
        BaseResponse<Object> bodyOfResponse = new BaseResponse<>(400, ERROR_INVALID_REQUEST);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return this.handleExceptionInternal(ex, bodyOfResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders responseHeaders, HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        log.error("inside controller advice of MethodArgumentNotValidException {}", message);
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        BaseResponse<Object> bodyOfResponse = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), message);
        return handleExceptionInternal(ex, bodyOfResponse, responseHeaders, HttpStatus.BAD_REQUEST, request);
    }

    private static HttpHeaders getResponseHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return responseHeaders;
    }

}
