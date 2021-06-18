package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserDetailRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.*;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserDetailService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EducationService educationService;
    @Autowired
    ExperienceService experienceService;
    @Autowired
    SkillService skillService;
    @Autowired
    UserProfileService userProfileService;

    public User addUserDetail(Integer id, UserDetailRequest userDetailRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));

        log.debug("Entering addUserDetail method");

        UserProfile userProfile = userDetailRequest.getUserProfile();
        educationService.addEducationDetails(id,userDetailRequest.getUserEducationRequest());
        experienceService.addUserExperienceDetails(id,userDetailRequest.getUserExperienceRequest());
        skillService.addUserSkills(id,userDetailRequest.getUserSkillRequest());
        userProfileService.addUserDetails(id,userDetailRequest.getUserProfile());

        return user;


    }
}
