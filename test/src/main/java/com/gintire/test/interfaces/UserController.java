package com.gintire.test.interfaces;

import com.gintire.test.application.UserService;
import com.gintire.test.domain.User;
import com.gintire.test.exception.MyResourceNotFoundException;
import com.gintire.test.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest/api/v1/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @GetMapping("/users")
    List<User> getAllUsers () {
        return userService.getAllUsers();
    }

    @GetMapping("/{name}")
    User getUser(@PathVariable("name") String name) {
        return userService.getUser(name);
    }

    @GetMapping(value = "/name/{id}")
    public String findById(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) {
        try {
            String username = userService.getUser("id").getName();

            return username;
        } catch (MyResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", exception);
        }
    }

    // 예외 처리
    @ExceptionHandler({UserException.class})
    public void handleException() {
        //
        logger.info("Exception Handled");
    }
}
