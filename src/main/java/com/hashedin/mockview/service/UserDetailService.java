package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.*;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.*;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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

        userRepository.updateProfileStatus(Boolean.TRUE, user.getId());
        return user;


    }

    public UserDetailResponse getUserDetails(Integer id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<UserEducation> userEducationList = educationService.getEducationDetails(user);
        List<UserWorkExperience> userWorkExperienceList = experienceService.getExperienceDetails(user);
        List<Skill> skillList = skillService.findSkillDetails(user);
        UserProfile userprofile = userProfileService.findProfileDetails(user);
        List<Award> awardList = awardService.findAwardDetails(user);


       return UserDetailResponse.builder()
                .userProfile(userprofile)
                .userAwardRequest(new UserAwardRequest(awardList))
                .userEducationRequest(new UserEducationRequest(userEducationList))
                .userSkillRequest(new UserSkillRequest(skillList))
                .userExperienceRequest(new UserExperienceRequest(userWorkExperienceList))
                .build();


    }

}
