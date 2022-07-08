package com.gintire.test.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.interfaces
 * <p>
 *
 * @author: jin36
 * @version: 21.01
 * Date: 2021-01-22
 * Time: 오후 5:21
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CustomErrorController implements ErrorController {
    Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    @Value("${server.error.path:${error.path:/error}}")
    private String VIEW_PATH;

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest httpServletRequest) {
        Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        logger.info(VIEW_PATH);

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return VIEW_PATH + "/404.html";
            }
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return VIEW_PATH + "/500";
            }
        }
        return "error";
    }


    @Override
    public String getErrorPath() {
        return null;
    }
}
