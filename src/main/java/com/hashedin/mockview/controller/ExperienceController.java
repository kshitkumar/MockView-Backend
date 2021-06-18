package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserExperienceRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.service.ExperienceService;
import com.hashedin.mockview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/profile/experience")
public class ExperienceController {

    @Autowired
    UserService userService;
    @Autowired
    ExperienceService experienceService;

    @PostMapping
    public ResponseEntity<Void> addExperienceDetails(@PathVariable("userId")Integer userId, @RequestBody UserExperienceRequest userExperienceRequest) throws ResourceNotFoundException {
        experienceService.addUserExperienceDetails(userId, userExperienceRequest);
            return new ResponseEntity<>( HttpStatus.CREATED);
    }
}
