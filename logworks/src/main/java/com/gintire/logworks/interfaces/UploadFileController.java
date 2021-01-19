package com.gintire.logworks.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: spring-boot-test
 * Package: com.gintire.logworks.interfaces
 * <p>
 * @author : jin36
 * date : 2021-01-19
 * Time: 오후 11:46
 * @version : 21.01
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value="/rest/api/v1/system", produces = "application/json")
public class UploadFileController {
    Logger logger = LoggerFactory.getLogger(UploadFileController.class);

}
