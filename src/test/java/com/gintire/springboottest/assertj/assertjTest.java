package com.gintire.springboottest.assertj;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.assertj
 * <p>
 * User: jin36
 * Date: 2021-01-10
 * Time: 오후 9:31
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */

import com.gintire.springboottest.domain.Gender;
import com.gintire.springboottest.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class assertjTest {
    List<User> fellowshipOfTheRing;
    User frodo;
    User sam;
    User sauron;
    @BeforeEach
    public void init() {
        frodo = new User(0, "frodo", 22, Gender.MALE);
        sam = new User(1, "sam", 23, Gender.MALE);
        sauron = new User(3, "sauron", 85, Gender.INTERSEX);
        fellowshipOfTheRing = Arrays.asList(frodo, sam);
    }
    // Assertj Core

    // basic assertions
    // chaining string specific assertions
    @Test
    public void basic() {
        assertThat("Hello, world! Nice to meet you.") // 주어진 "Hello, world! Nice to meet you."라는 문자열은
                .isNotEmpty() // 비어있지 않고
                .contains("Nice") // "Nice"를 포함하고
                .contains("world") // "world"도 포함하고
                .doesNotContain("ZZZ") // "ZZZ"는 포함하지 않으며
                .startsWith("Hell") // "Hell"로 시작하고
                .endsWith("u.") // "u."로 끝나며
                .isEqualTo("Hello, world! Nice to meet you."); // "Hello, world! Nice to meet you."과 일치합니다
    }
    // collection specific assertions ( there are plenty more)
    // in the examples below fellowshipTheRing is a List<TolkienCharacter>
    @Test
    public void list_assertions() {
        assertThat(fellowshipOfTheRing)
                .hasSize(2)
                .contains(frodo, sam)
                .doesNotContain(sauron);
    }

}
