package com.hashedin.mockview.controller;

import com.hashedin.mockview.dto.UserSkillRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.service.SkillService;
import com.hashedin.mockview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/profile/skill")
public class SkillController {
    @Autowired
    SkillService skillService;
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity addUserSkill(@PathVariable("userId") Integer userId, @RequestBody UserSkillRequest userSkillRequest) throws ResourceNotFoundException {
        skillService.addUserSkills(userId, userSkillRequest);
        return new ResponseEntity( HttpStatus.CREATED);
    }
}
