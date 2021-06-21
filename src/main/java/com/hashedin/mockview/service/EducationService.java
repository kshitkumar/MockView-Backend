package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserEducationRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserEducation;
import com.hashedin.mockview.repository.UserEducationRepository;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EducationService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserEducationRepository userEducationRepository;

    public List<UserEducation> addEducationDetails(User user, UserEducationRequest userEducationRequest) throws ResourceNotFoundException {

        log.debug("Entering addEducationDetails method");
        List<UserEducation> inputUserEducationList = userEducationRequest.getUserEducationList();
        List<UserEducation> userEducationList = inputUserEducationList.stream().map(x -> x.builder()
                .degreeName(x.getDegreeName())
                .stream(x.getStream())
                .marks(x.getMarks())
                .year(x.getYear())
                .schoolName(x.getSchoolName())
                .country(x.getCountry())
                .educationCategory(x.getEducationCategory())
                .user(user)
                .build()).collect(Collectors.toList());

        return userEducationRepository.saveAll(userEducationList);

    }

    public List<UserEducation> getEducationDetails(User user) throws ResourceNotFoundException {

        log.debug("Entering getEducationDetails method");
        return userEducationRepository.findByUser(user);


    }

}
