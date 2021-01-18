package com.gintire.test.domain;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.domain
 * <p>
 * User: jin36
 * Date: 2021-01-07
 * Time: 오후 7:41
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int div(int a, int b) {
        if (b == 0) throw new ArithmeticException("/ by zero");
        return a / b;
    }
}
