package com.gintire.test.mockito;

import com.gintire.test.application.UserService;
import com.gintire.test.domain.Gender;
import com.gintire.test.domain.User;
import com.gintire.test.infrastructure.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.mockito
 * <p>
 * User: jin36
 * Date: 2021-01-18
 * Time: 오후 8:30
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ExtendWith(MockitoExtension.class)
public class UserStubTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void init() {
        // original Mockito
        when(userRepository.getUser("james")).thenReturn(new User(1, "Tom", 20, Gender.MALE));

        // BDDMockito
        given(userRepository.getUser("paul")).willReturn(new User(2, "Tomas", 44, Gender.MALE));

        // throw stub
        // non-void return type
        when(userRepository.getUser("lily"))
                .thenThrow(RuntimeException.class);

/*        // void return type
        doThrow(RuntimeException.class)
                .when(userRepository)
                .putUser(null);*/
    }

    @Test
    public void 제임스_유저_가져오기_테스트() {
        // given
        // when
        // then
        /**
         * UserRepository를 보면, 만약 james라는 유저를 가져온다고 하면, 아래의 유저를 가져오게 된다.
         * users.add(new User(0, "james", 32, Gender.MALE));
         *
         * 하지만 mock을 생성하여 UserService에 주입하게 된다.
         * @InjectMocks를 사용하는 경우, 위의 예를 따르면,
         * UserService안에 UserRepository가 있을 때, 이 때, UserRepository가 주입받고 난 후에 UserService가 생성되어야 하는데 이 때 사용이 용이하다.
         */

        // original Mockito
        userService.getUser("james");
        verify(userRepository)
                .getUser("james");
        Assertions.assertEquals(userService.getUser("james"), new User(1, "Tom", 20, Gender.MALE));


        // BDD mockito
        userService.getUser("paul");
        then(userRepository)
                .should()
                .getUser("paul");
        Assertions.assertEquals(userService.getUser("paul"), new User(2, "Tomas", 44, Gender.MALE));

        // 유저를 가져오는데, 아무 값도 넣지 않는다면, 동작하지 않음
        userService.getUser("");
        then(userRepository)
                .should(never())
                .getUser("");

        // 예외 처리
        Assertions.assertThrows(RuntimeException.class, () -> userService.getUser("lily"));

        Assertions.assertThrows(RuntimeException.class, () -> userService.setUser(null));
    }

}
