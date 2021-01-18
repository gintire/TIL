package com.gintire.springboottest.infrastructure;

import com.gintire.springboottest.domain.Gender;
import com.gintire.springboottest.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    public List<User> users = new ArrayList<>();
    private UserRepository() {
        users.add(new User(0, "james", 32, Gender.MALE));
        users.add(new User(1, "paul", 49, Gender.INTERSEX));
        users.add(new User(2, "ariana", 22, Gender.FEMALE));
        users.add(new User(3, "dave", 54, Gender.MALE));
        users.add(new User(4, "luka", 23, Gender.MALE));
    }

    public List<User> getUsers() {
        return users;
    }
    public User getUser(String name) {
        for (User user : users) {
            if(user.getName().equals(name)) return user;
        }
        return null;
    }
    public void putUser(User user) {users.add(user);}
}
