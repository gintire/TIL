package com.gintire.test.interfaces;

import com.gintire.test.application.UserService;
import com.gintire.test.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    List<User> getAllUsers () {
        return userService.getAllUsers();
    }
}
