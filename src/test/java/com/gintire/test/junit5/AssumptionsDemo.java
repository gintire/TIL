package com.gintire.springboottest.junit5;

import com.gintire.springboottest.domain.Calculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.junit5
 * <p>
 * User: jin36
 * Date: 2021-01-07
 * Time: 오후 8:34
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 * <p>
 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions
 */
public class AssumptionsDemo {
    private final Calculator calculator = new Calculator();

    @Test
    void testOnlyOnLocalServer() {
        System.out.println(System.getenv("ENV"));
        assumeTrue("local".equals(System.getenv("ENV")));
        // remainder of test
    }

    @Test
    void testOnlyOnDeveloperWorkstation() {
        assumeTrue("DEV".equals(System.getenv("ENV")),
                () -> "Aborting test: not on developer workstation");
    }

    @Test
    void testInAllEnvironments() {
        assumingThat("local".equals(System.getenv("ENV")),
                () -> {
                    //perform these assertions only on the local server
                    assertEquals(2, calculator.div(4, 2));
                });
        // perform these assertions in all environments
        assertEquals(42, calculator.multiply(6, 7));
    }
}
