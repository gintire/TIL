package com.gintire.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.exception
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-22
 * Time: 오후 2:20
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MyResourceNotFoundException extends RuntimeException {
    public MyResourceNotFoundException() {
        super();
    }

    public MyResourceNotFoundException(String message) {
        super(message);
    }

    public MyResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
