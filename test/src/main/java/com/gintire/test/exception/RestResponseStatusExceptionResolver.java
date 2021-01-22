package com.gintire.test.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.exception
 * <p>
 *
 * @author: jin36
 * @version: 21.01
 * Time: 오후 2:36
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RestResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler,
            Exception exception) {
        try {
            if(exception instanceof IllegalArgumentException) {
                return handleIllegalArgument(
                        (IllegalArgumentException) exception,
                        httpServletRequest,
                        httpServletResponse);
            }
            // ...
        } catch (Exception handlerException) {
            logger.warn("Handling of [" + exception.getClass().getName() + "] resulted in Exception", handlerException);
        }
        return null;
    }

    private ModelAndView
    handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_CONFLICT);
        String accept = httpServletRequest.getHeader(HttpHeaders.ACCEPT);
        // ...
        return new ModelAndView();
    }
}
