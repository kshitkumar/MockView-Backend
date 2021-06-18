package com.hashedin.mockview.controller;

import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/profile")
public class ProfileController {

    @Autowired
    UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<Void> addDetails(@PathVariable("userId") Integer id, @RequestBody UserProfile userProfile) throws ResourceNotFoundException {
        userProfileService.addUserDetails(id,userProfile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
