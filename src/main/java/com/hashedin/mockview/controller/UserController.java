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
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody User user) {
        logger.debug("Input User object is {}", user);
        return userService.userSignUp(user);
    }

    @GetMapping("/user")
    public ResponseEntity loginUser(@RequestBody UserInputRequest userInputRequest) {
        return userService.loginUser(userInputRequest);
    }

    @PostMapping("/user/details/{id}")
    public ResponseEntity addDetails(@PathVariable int id, @RequestBody UserProfile userProfile) {
        return userService.addUserDetails(id, userProfile);
    }

    @PostMapping("/user/details/education/{id}")
    public ResponseEntity addEducationDetails(@PathVariable Integer id, @RequestBody UserEducationRequest userEducationRequest) {
        return userService.addEducationDetails(id, userEducationRequest);
    }

    @PostMapping("/user/details/experience/{id}")
    public ResponseEntity addExperienceDetails(@PathVariable Integer id, @RequestBody UserExperienceRequest userExperienceRequest) {
        return userService.addUserExperienceDetails(id,userExperienceRequest);
    }
    @PostMapping("/user/details/skill/{id}")
    public ResponseEntity addUserSkill(@PathVariable Integer id, @RequestBody UserSkillRequest userSkillRequest)
    {
        return userService.addUserSkills(id,userSkillRequest);
    }


}
