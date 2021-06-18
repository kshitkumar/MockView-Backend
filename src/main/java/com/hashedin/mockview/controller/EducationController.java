package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserEducationRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.service.EducationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/profile/education")
public class EducationController {

    @Autowired
    EducationService educationService;

    @PostMapping
    public ResponseEntity<Void> addEducationDetails(@PathVariable("userId") Integer userId, @RequestBody UserEducationRequest userEducationRequest) throws ResourceNotFoundException {
        if (userEducationRequest == null)
            return ResponseEntity.noContent().build();
        educationService.addEducationDetails(userId, userEducationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
