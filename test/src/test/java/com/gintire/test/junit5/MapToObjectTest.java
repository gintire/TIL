package com.gintire.test.junit5;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Objects;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.junit5
 * <p>
 *
 * @author: jin36
 * @version: 2021.04
 * Date: 2021-04-06
 * Time: 오후 4:34
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class MapToObjectTest {
    static HashMap<String, String> map;
    @BeforeClass
    public static void init() {
        map = new HashMap<>();
        map.put("last_name", "Beckham");
        map.put("first_name", "David");

    }

    @Test
    public void checkMapToObject() {
        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        final User user = mapper.convertValue(map, User.class);

        User expectUser = new User("Beckham", "David");
        Assertions.assertEquals(expectUser, user);

        System.out.println(user);
    }
}
class User {
    final String last_name;
    final String first_name;

    @ConstructorProperties({"last_name", "first_name"})
    public User(String last_name, String first_name) {
        this.last_name = last_name;
        this.first_name = first_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(last_name, user.last_name) &&
                Objects.equals(first_name, user.first_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(last_name, first_name);
    }

    @Override
    public String toString() {
        return "User{" +
                "last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                '}';
    }
}
