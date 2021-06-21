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
    @Autowired
    AwardService awardService;

    public User addUserDetail(Integer id, UserDetailRequest userDetailRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));

        log.debug("Entering addUserDetail method");

        if (userDetailRequest.getUserEducationRequest() != null)
            educationService.addEducationDetails(user, userDetailRequest.getUserEducationRequest());
        if (userDetailRequest.getUserExperienceRequest() != null)
            experienceService.addUserExperienceDetails(user, userDetailRequest.getUserExperienceRequest());
        if (userDetailRequest.getUserSkillRequest() != null)
            skillService.addUserSkills(user, userDetailRequest.getUserSkillRequest());
        if (userDetailRequest.getUserProfile() != null)
            userProfileService.addUserDetails(user, userDetailRequest.getUserProfile());
        if (userDetailRequest.getUserAwardRequest() != null)
            awardService.addAwardDetails(user, userDetailRequest.getUserAwardRequest());

        userRepository.updateProfileStatus(Boolean.TRUE,user.getId());
        return user;


    }

}
