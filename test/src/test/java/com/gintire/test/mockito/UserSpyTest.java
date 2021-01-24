package com.gintire.test.mockito;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Project: spring-boot-test
 * Package: com.gintire.test.mockito
 * <p>
 *
 * @author: jin36
 * @version: 21.01
 * Date: 2021-01-24
 * Time: 오후 11:23
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class UserSpyTest {
    @Test
    public void whenSpyingOnList_thenCorrect() {
        List list = new LinkedList();
        List spyList = Mockito.spy(list);

        spyList.add("one");
        spyList.add("two");

        Mockito.verify(spyList).add("one");
        Mockito.verify(spyList).add("two");

        assertEquals(2, spyList.size());
    }



}
