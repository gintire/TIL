package com.gintire.test.mockito;

import com.gintire.test.application.UserService;
import com.gintire.test.domain.Gender;
import com.gintire.test.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.mockito
 * <p>
 * User: jin36
 * Date: 2021-01-17
 * Time: 오후 2:17
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class UserTest {
    UserService userService = mock(UserService.class);
    List mockedList = mock(List.class);
    @BeforeEach
    public void 초기화 () {
        // stubbing
        when(userService.getUser("james")).thenReturn(new User(1, "James", 19, Gender.INTERSEX));
    }
    @Test
    public void 전체_유저_확인() {
        // mock


        // mock 사용하기
        userService.getAllUsers();
        mockedList.add("one");
        mockedList.clear();

        // verification
        verify(mockedList).add("one");
        verify(mockedList).clear();

        verify(userService).getAllUsers();
    }

    @Test
    public void stubbing을이용한_유저_테스트() {
        System.out.println(userService.getUser("james"));
        verify(userService).getUser("james");
        Assertions.assertThat(new User(1, "James", 19, Gender.INTERSEX)).isEqualTo(userService.getUser("james"));
    }

    /**
     * void가 리턴인 값에 대한 mockito Test
     */
    @Test
    public void whenAddCalledVerified() {
        MyList myList = mock(MyList.class);
        // doNothing()은 Mockito의 void 메서드에 대한 기본 설정이다.
        //doNothing().when(myList).add(isA(Integer.class), isA(String.class));
        myList.add(0, "");

        verify(myList, times(1)).add(0, "");
    }

    @Test
    public void whenAddCalledValueCaptured() {
        MyList myList = mock(MyList.class);
        // doNothing()은 Mockito의 void 메서드에 대한 기본 설정이다.
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(myList).add(isA(Integer.class), valueCapture.capture());
        myList.add(0, "captured");

        Assert.assertEquals("captured", valueCapture.getValue());
    }

    @Test
    public void whenAddCalledAnswered() {
        MyList myList = mock(MyList.class);
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);

            Assert.assertEquals(3, arg0);
            Assert.assertEquals("answer me", arg1);
            return null;
        }).when(myList).add(any(Integer.class), any(String.class));
        myList.add(3, "answer me");
    }

    @Test
    public void whenAddCalledRealMethodCalled() {
        MyList myList = mock(MyList.class);
        doCallRealMethod().when(myList).add(any(Integer.class), any(String.class));
        myList.add(1, "real");

        verify(myList, times(1)).add(1, "real");
    }

    @Test
    public void whenAddCalledException() {
        MyList myList = mock(MyList.class);
        doThrow(IllegalArgumentException.class).when(myList).add(0, null);
        Assert.assertThrows(IllegalArgumentException.class, () -> myList.add(0, null));
    }
}
