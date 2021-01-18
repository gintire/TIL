package com.gintire.springboottest.mockito;

import java.util.AbstractList;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.mockito
 * <p>
 * User: jin36
 * Date: 2021-01-17
 * Time: 오후 9:09
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class MyList extends AbstractList<String> {

    @Override
    public String get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void add(int index, String element) {}
}
