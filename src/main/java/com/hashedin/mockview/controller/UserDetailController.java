package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserAuthenticationResponse;
import com.hashedin.mockview.dto.UserEducationRequest;
import com.hashedin.mockview.dto.UserExperienceRequest;
import com.hashedin.mockview.dto.UserSkillRequest;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/details")
public class UserDetailController {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailController.class);

    @Autowired
    UserService userService;


    @PostMapping
    public ResponseEntity addDetails(@RequestBody UserProfile userProfile) {
        String userName = userService.getUserNameForCurrentUser();
        String jwt =  userService.addUserDetails(userName, userProfile);
        return new ResponseEntity(new UserAuthenticationResponse(jwt), HttpStatus.OK);
    }

    @PostMapping("/education")
    public ResponseEntity addEducationDetails(@RequestBody UserEducationRequest userEducationRequest) {
        String userName = userService.getUserNameForCurrentUser();
        String jwt = userService.addEducationDetails(userName, userEducationRequest);
        return new ResponseEntity(new UserAuthenticationResponse(jwt), HttpStatus.OK);
    }

    @PostMapping("/experience")
    public ResponseEntity addExperienceDetails(@RequestBody UserExperienceRequest userExperienceRequest) {
        String userName = userService.getUserNameForCurrentUser();
        String jwt=  userService.addUserExperienceDetails(userName, userExperienceRequest);
        if(jwt != null)
            return new ResponseEntity(new UserAuthenticationResponse(jwt), HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/skill")
    public ResponseEntity addUserSkill(@RequestBody UserSkillRequest userSkillRequest) {
        String userName = userService.getUserNameForCurrentUser();
        String jwt = userService.addUserSkills(userName, userSkillRequest);
        return new ResponseEntity(new UserAuthenticationResponse(jwt), HttpStatus.OK);
    }
}
