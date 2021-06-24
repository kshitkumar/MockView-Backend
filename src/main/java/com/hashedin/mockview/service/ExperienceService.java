package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserExperienceRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Industry;
import com.hashedin.mockview.model.Position;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserWorkExperience;
import com.hashedin.mockview.repository.UserRepository;
import com.hashedin.mockview.repository.UserWorkExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        inputUserWorkExperienceList.stream().forEach(item -> item.setUser(user));
        userWorkExperienceRepository.saveAll(inputUserWorkExperienceList);
        log.debug("User experience details successfully inserted in database ");


    }
    public static boolean filterForIndustry(Industry industry, UserWorkExperience userWorkExperience) {
        if (industry != null) {
            if (userWorkExperience.getIndustry().equals(industry))
                return true;
            else
                return false;
        }
        return true;
    }

    public static boolean filterForCompany(String company, UserWorkExperience userWorkExperience) {
        if (company != null) {
            if (userWorkExperience.getCompanyName().equals(company))
                return true;
            else
                return false;
        }
        return true;
    }

    public static boolean filterForPosition(Position position, UserWorkExperience userWorkExperience) {
        if (position != null) {
            if (userWorkExperience.getPosition().equals(position))
                return true;
            else
                return false;
        }
        return true;
    }

    public Double calculateUserExperience(List<UserWorkExperience> workExperience) {
        Double monthExperience = 0.0;

        for (UserWorkExperience experience : workExperience) {
            //not current employment
            if (experience.getEndingDate() != null) {
                int m1 = experience.getJoiningDate().getYear() * 12 + experience.getJoiningDate().getMonth();

                int m2 = experience.getEndingDate().getYear() * 12 + experience.getEndingDate().getMonth();
                monthExperience += m2 - m1 + 1;
            }
            //is current employment
            else {

                //Experience calculation Logic

                java.util.Date currentDate = new java.util.Date();


                int m1 = experience.getJoiningDate().getYear() * 12 + experience.getJoiningDate().getMonth();
                int m2 = currentDate.getYear() * 12 + currentDate.getMonth();
                monthExperience += m2 - m1 + 1;

            }
        }
        return monthExperience / 12;
    }

    public UserWorkExperience getCurrentCompany(List<UserWorkExperience> userWorkExperienceList) {
        Optional<UserWorkExperience> optionalUserWorkExperience = userWorkExperienceList.stream().filter(x -> x.getCurrentEmployment().equals(Boolean.TRUE)).findFirst();
        if (optionalUserWorkExperience.isPresent())
            return optionalUserWorkExperience.get();
        else
            return null;
    }

    public List<UserWorkExperience> getExperienceDetails(User user) {
        log.debug("Entering getExperienceDetails method");
        return userWorkExperienceRepository.findByUser(user);
    }
}
