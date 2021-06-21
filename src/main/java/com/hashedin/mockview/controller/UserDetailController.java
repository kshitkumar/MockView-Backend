package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserDetailRequest;
import com.hashedin.mockview.dto.UserDetailResponse;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/detail")
@Slf4j
@CrossOrigin
public class UserDetailController {

    @Autowired
    UserDetailService userDetailService;

    @PostMapping
    public ResponseEntity<Void> addUserDetails(@PathVariable("userId")Integer userId ,@RequestBody UserDetailRequest userDetailRequest) throws ResourceNotFoundException {
        userDetailService.addUserDetail(userId,userDetailRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<UserDetailResponse> getUserDetails(@PathVariable("userId")Integer userId) throws ResourceNotFoundException {
        UserDetailResponse userDetailResponse = userDetailService.getUserDetails(userId);
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }



}
