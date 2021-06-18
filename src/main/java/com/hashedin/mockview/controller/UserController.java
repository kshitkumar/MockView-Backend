package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.*;
import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.DuplicateResourceException;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @PostMapping("/authenticate")
    public ResponseEntity createAuthenticationToken(@RequestBody UserInputRequest userInputRequest) throws Exception {
         String jwt = userService.createAuthenticationToken(userInputRequest);
        return new ResponseEntity(new UserAuthenticationResponse(jwt), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody User user) throws DuplicateResourceException, BadRequestException {
        logger.debug("Input User object is {}", user);

        User returnedUser =   userService.userSignUp(user);
        if(returnedUser != null)
        return new ResponseEntity(returnedUser,HttpStatus.CREATED);
        else
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }




//    @GetMapping("//details")
//    public ResponseEntity getUserProfileDetails()
//    {
//        String userName = userService.getUserNameForCurrentUser();
//        return userService.getUserDetails(userName);
//    }


}
