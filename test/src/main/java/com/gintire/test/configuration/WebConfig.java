package com.gintire.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.configuration
 * <p>
 * User: jin36
 * Date: 2021-01-19
 * Time: 오후 9:59
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/test/com.gintire.rosemary.resources")
                .setCachePeriod(20);
    }
}
