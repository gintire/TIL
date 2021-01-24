package com.gintire.test.mockito;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.ArrayList;
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
 *
 * ref : https://www.baeldung.com/mockito-spy
 */
public class UserSpyTest {
    /**
     * Simple Spy Example
     */
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

    /**
     * Spy annotation
     */
    @Spy
    List<String> spyList = new ArrayList<>();

    @Test
    public void whenUsingTheSpyAnnotation_thenObjectIsSpied() {
        spyList.add("one");
        spyList.add("two");

        Mockito.verify(spyList).add("one");
        Mockito.verify(spyList).add("two");

        assertEquals(2, spyList.size());
    }

    /**
     * Stubbing a Spy
     */
    @Test
    public void whenStubASpy_thenStubbed() {
        List<String> list = new ArrayList<>();
        List<String> spyList = Mockito.spy(list);

        assertEquals(0, spyList.size());

        Mockito.doReturn(100).when(spyList).size();
        assertEquals(100, spyList.size());
    }

    /**
     * Mock vs Spy in Mockito
     * Mock : Mockito가 mock을 생성하면, 실제 인스턴스로부터 생성하는 것이 아니고 클래스의 Type으로 부터 가져온다.
     * mock은 단순히 클래스의 bare-bones shell instance를 생성하며, 상호 작용을 추적하기 위해 완전히 계측된다.
     * Spy : spy는 존재하는 인스턴스를 감싼다.
     * 이것은 일반 인스턴스와 동일한 방식으로 동작한다.
     * 유일한 차이점은 모든 상호 작용을 추적하도록 계측된다는 것이다.
     **/

    @Test
    public void whenCreateMock_thenCreated() {
        /**
         * 아래 테스트에서 볼 수 있듯이 MOCK은 list에 실제로는 아무것도 추가히자 않는다.
         * 다른 사이드 이펙트 없이 메서드를 호출한다.
         */
        List mockedList = Mockito.mock(ArrayList.class);

        mockedList.add("one");
        Mockito.verify(mockedList).add("one");

        assertEquals(0, mockedList.size());
    }

    @Test
    public void whenCreateSpy_thenCreate() {
        /**
         * 반면에 스파이는 다르게 동작합니다.
         * 실제로 add 메서드의 실제 구현을 호출하고 기본 목록에 요소를 추가합니다.
         */
        List spyList = Mockito.spy(new ArrayList());

        spyList.add("one");
        Mockito.verify(spyList).add("one");

        assertEquals(1, spyList.size());
    }
    /**
     * Mockito NotAMockException 이해
     *
     * mocks 또는 spies를 잘못사용할 때, 발생하는 흔한 예외중 하나인 NotAMockException에 대해 이해
     */
    @Test
    public void whenNotAMockException() {
        /*List<String> list = new ArrayList<>();
        Mockito.doReturn(100).when(list).size();

        org.springframework.test.util.AssertionErrors.assertEquals("Size should be 100: ", 100, list.size());*/
        /**
         * 결과
         * org.mockito.exceptions.misusing.NotAMockException:
         * Argument passed to when() is not a mock!
         * Example of correct stubbing:
         *     doThrow(new RuntimeException()).when(mock).someMethod();
         *
         * Mockito when() 메서드는 mock 또는 spy 오브젝트를 인자로 받음
         */

        final List<String> list = Mockito.spy(new ArrayList<>());
        Mockito.doReturn(100).when(list).size();

        org.springframework.test.util.AssertionErrors.assertEquals("Size should be 100: ", 100, list.size());
    }

}
