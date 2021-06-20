package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserDto;
import com.hashedin.mockview.dto.UserInputRequest;
import com.hashedin.mockview.exception.InvalidPasswordException;
import com.hashedin.mockview.service.AuthenticationService;
import com.hashedin.mockview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin
public class LoginController {


    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> createAuthenticationToken(@RequestBody UserInputRequest userInputRequest) throws InvalidPasswordException {

        String jwt = authenticationService.createAuthenticationToken(userInputRequest);
        UserDto user = userService.getUserDetails(userInputRequest.getEmailId());
        user.setJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }








}
