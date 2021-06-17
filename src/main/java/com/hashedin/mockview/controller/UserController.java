package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserEducationRequest;
import com.hashedin.mockview.dto.UserExperienceRequest;
import com.hashedin.mockview.dto.UserInputRequest;
import com.hashedin.mockview.dto.UserSkillRequest;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @PostMapping("/authenticate")
    public ResponseEntity createAuthenticationToken(@RequestBody UserInputRequest userInputRequest) throws Exception {
        return userService.createAuthenticationToken(userInputRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity createUser(@RequestBody User user) {
        logger.debug("Input User object is {}", user);
        return userService.userSignUp(user);
    }

//    @GetMapping("/user")
//    public ResponseEntity loginUser(@RequestBody UserInputRequest userInputRequest) {
//        return userService.loginUser(userInputRequest);
//    }

    @PostMapping("/user/details")
    public ResponseEntity addDetails(@RequestBody UserProfile userProfile) {
        String userName = userService.getUserNameForCurrentUser();
        return userService.addUserDetails(userName, userProfile);
    }

    @PostMapping("/user/details/education")
    public ResponseEntity addEducationDetails(@RequestBody UserEducationRequest userEducationRequest) {
        String userName = userService.getUserNameForCurrentUser();
        return userService.addEducationDetails(userName, userEducationRequest);
    }

    @PostMapping("/user/details/experience")
    public ResponseEntity addExperienceDetails(@RequestBody UserExperienceRequest userExperienceRequest) {
        String userName = userService.getUserNameForCurrentUser();
        return userService.addUserExperienceDetails(userName, userExperienceRequest);
    }

    @PostMapping("/user/details/skill")
    public ResponseEntity addUserSkill(@RequestBody UserSkillRequest userSkillRequest) {
        String userName = userService.getUserNameForCurrentUser();
        return userService.addUserSkills(userName, userSkillRequest);
    }


}
