package com.gintire.springboottest.junit5.utils;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.junit5.utils
 * <p>
 * User: jin36
 * Date: 2021-01-09
 * Time: 오후 1:03
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class MyParallelExecutionContifurationStrategy implements ParallelExecutionConfigurationStrategy {

    @Override
    public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
        return null;
    }
}
