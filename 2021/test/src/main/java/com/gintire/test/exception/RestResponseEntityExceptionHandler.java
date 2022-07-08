package com.gintire.test.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.exception
 * <p>
 *
 * @author: jin36
 * @version: Date: 21.01
 * Time: 오후 3:12
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler
extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value ={IllegalArgumentException.class,
    IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict (
            RuntimeException ex, WebRequest request
    ) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
