package com.gintire.test.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.exception
 * <p>
 *
 * @author: jin36
 * @version: 21.01
 * Time: 오전 11:24
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class UserException extends RuntimeException {
    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }
}
