package com.gintire.pure.week11;

/**
 * Project: spring-boot-test
 * Package: com.gintire.pure.week11
 * <p>
 *
 * @author: jin36
 * @version: Date: 2021-01-30
 * Time: 오후 11:14
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Herb {
    enum Type {ANNUAL, PERENNIAL, BIENNIAL}
    final String name;
    final Type type;

    public Herb(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
