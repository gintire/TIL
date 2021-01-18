package com.gintire.springboottest.junit5;

import com.gintire.springboottest.application.UserService;
import com.gintire.springboottest.domain.Fixture;
import com.gintire.springboottest.domain.Gender;
import com.gintire.springboottest.domain.Shape;
import com.gintire.springboottest.domain.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserService userService;

    public List<User> users = new ArrayList<>();

    @BeforeEach
    void 초기화() {
        users.add(new User(0, "james", 32, Gender.MALE));
        users.add(new User(1, "paul", 49, Gender.INTERSEX));
        users.add(new User(2, "ariana", 22, Gender.FEMALE));
        users.add(new User(3, "dave", 54, Gender.MALE));
        users.add(new User(4, "luka", 23, Gender.MALE));
    }

    @Test
    void 간단한Assertion() {
        assertEquals(1, 1);
    }

    @Test
    @Disabled("Not implemented yet")
    void 테스트_건너뛰기() {
        assertEquals(1, 1);
    }

    @Test
    @DisplayName("리스트내모든값확인")
    void 리스트내모든값확인() {
        List<Integer> numbers = List.of(2, 3, 5, 7);
        assertAll(() -> assertEquals(2, numbers.get(0)),
                () -> assertEquals(3, numbers.get(1)),
                () -> assertEquals(5, numbers.get(2)),
                () -> assertEquals(7, numbers.get(3)));
    }

    @Test
    @DisplayName("Should only run the test if some criteria are met")
    void shouldOnlyRunTheTestIfSomeCriteriaAreMet() {
        Assumptions.assumeTrue(Fixture.apiVersion() < 10);
        // these tests only apply to a recent API version
        assertEquals(1, 1);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Should create shapes with different numbers of sides")
    @ValueSource(ints = {3, 4, 5, 8, 14})
    void shouldCreateShapesWithDifferentNumbersOfSides(int expectedNumberOfSides) {
        Shape shape = new Shape(expectedNumberOfSides);
        assertEquals(expectedNumberOfSides, shape.numberOfSides());
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Should create shapes with different numbers of sides")
    @ValueSource(ints = {Integer.MAX_VALUE, Integer.MIN_VALUE})
    void shouldCreateShapesWithDifferentNumbersOfSidesException(int expectedNumberOfSides) {
        assertThrows(IllegalArgumentException.class,
                () -> new Shape(expectedNumberOfSides));
    }

    @Test
    void 모든유저테스트() {
        assertEquals(users, userService.getAllUsers());
    }

    @Test
    void 유저검색테스트() {
        assertEquals(userService.getUser("james"), new User(0, "james", 32, Gender.MALE));
    }
}

class ExampleTest {
    @Nested
    @DisplayName("When a shape has been created")
    class WhenShapeExists {
        private final Shape shape = new Shape(4);

        @Nested
        @DisplayName("Should allow")
        class ShouldAllow {
            @Test
            @DisplayName("seeing the number of sides")
            void seeingTheNumberOfSides() {
                assertEquals(4, shape.numberOfSides());
            }

            @Test
            @DisplayName("Seeing the description")
            void seeingTheDescription() {
                assertEquals("Square", shape.description());
            }
        }

        @Nested
        @DisplayName("Should not")
        class ShouldNot {
            @Test
            @DisplayName("be equal to another shape with the same number of sides")
            void beEqualToAnotherShapeWithTheSameNumberOfSides() {
                assertNotEquals(new Shape(4), shape);
            }
        }
    }
}
