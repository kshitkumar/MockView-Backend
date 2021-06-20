package com.hashedin.mockview.controller;

import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.DuplicateResourceException;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody User user) throws DuplicateResourceException, BadRequestException {
        log.debug("Input User object is {}", user);

        User returnedUser =   userService.userSignUp(user);
        if(returnedUser != null)
            return new ResponseEntity<>(HttpStatus.CREATED);
        else
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }
}
