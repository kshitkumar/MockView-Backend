package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserExperienceRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserWorkExperience;
import com.hashedin.mockview.repository.UserRepository;
import com.hashedin.mockview.repository.UserWorkExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExperienceService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserWorkExperienceRepository userWorkExperienceRepository;

    public void addUserExperienceDetails(User user, UserExperienceRequest userExperienceRequest) throws ResourceNotFoundException {
        log.debug("Entering addUserExperienceDetails");
        List<UserWorkExperience> inputUserWorkExperienceList = userExperienceRequest.getUserWorkExperienceList();
        List<UserWorkExperience> userWorkExperienceList = inputUserWorkExperienceList.stream()
                .map(x -> x.builder()
                        .companyName(x.getCompanyName())
                        .currentEmployment(x.getCurrentEmployment())
                        .endingDate(x.getEndingDate())
                        .industry(x.getIndustry())
                        .user(user)
                        .joiningDate(x.getJoiningDate())
                        .responsibility(x.getResponsibility())
                        .role(x.getRole())
                        .position(x.getPosition())
                        .build())
                .collect(Collectors.toList());
        userWorkExperienceRepository.saveAll(userWorkExperienceList);
        log.debug("User experience details successfully inserted in database ");


    }
}
