package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserEducationRequest;
import com.hashedin.mockview.dto.UserExperienceRequest;
import com.hashedin.mockview.dto.UserInputRequest;
import com.hashedin.mockview.dto.UserSkillRequest;
import com.hashedin.mockview.model.*;
import com.hashedin.mockview.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    UserEducationRepository userEducationRepository;
    @Autowired
    UserWorkExperienceRepository userWorkExperienceRepository;
    @Autowired
    SkillRepository skillRepository;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public ResponseEntity userSignUp(User inputUser) {

        logger.debug("Entering userSignUp method");
        try {
            User user = userRepository.save(inputUser);
            logger.debug("Saved user {} ", user);
            return new ResponseEntity(user.getId(), HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.debug("Exception occurred while saving");
            return new ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST);
        }


    }

    public ResponseEntity addUserDetails(Integer id, UserProfile inputUserProfile) {
        logger.debug("Entered updateUserDetails Method");
        try {
            User savedUser = userRepository.findById(id).get();
            if (savedUser != null) {
                UserProfile userProfile = UserProfile.builder()
                        .user(savedUser)
                        .city(inputUserProfile.getCity())
                        .country(inputUserProfile.getCountry())
                        .state(inputUserProfile.getState())
                        .pinCode(inputUserProfile.getPinCode())
                        .build();
                UserProfile savedUserProfile = userProfileRepository.save(userProfile);
                return new ResponseEntity(savedUser.getId(), HttpStatus.ACCEPTED);
            }
            logger.error("User details not Found");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            logger.error("Exception occured {}", ex.toString());
            return new ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity loginUser(UserInputRequest userInputRequest) {
        logger.debug("Entering loginUser Method");
        //User logging in via Email Id
        if (userInputRequest.getEmailId() != null) {
            User user = userRepository.findByEmailIdAndPassword(userInputRequest.getEmailId(), userInputRequest.getPassword());

            if (user != null) {
                logger.debug("Login Successful via Email Id");
                return new ResponseEntity(user.getId(), HttpStatus.OK);
            } else
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else if (userInputRequest.getPhoneNumber() != null) {
            User user = userRepository.findByPhoneNumberAndPassword(userInputRequest.getPhoneNumber(), userInputRequest.getPassword());

            if (user != null) {
                logger.debug("Login Successful via Phone Number");
                return new ResponseEntity(user.getId(), HttpStatus.OK);
            } else
                return new ResponseEntity(HttpStatus.BAD_REQUEST);


        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity addEducationDetails(Integer id, UserEducationRequest userEducationRequest) {

        logger.debug("Entering addEducationDetails method");
        if (id == null) {
            logger.debug("UserId not found in Input request");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findById(id).get();
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


        userEducationRepository.saveAll(userEducationList);

        return new ResponseEntity(user.getId(), HttpStatus.OK);


    }

    public ResponseEntity addUserExperienceDetails(Integer id, UserExperienceRequest userExperienceRequest) {
        logger.debug("Entering addUserExperienceDetails");
        User user = userRepository.findById(id).get();
        if (user == null) {
            logger.debug("User details corresponding to User Id {} not found", id);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            UserWorkExperience userWorkExperience = new UserWorkExperience();
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
            logger.debug("User id {} experience details successfully inserted in database ", id);
            return new ResponseEntity(user.getId(), HttpStatus.OK);

        } catch (Exception exception) {
            logger.error("Exception occurred" + exception.toString());
            exception.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity addUserSkills(Integer id, UserSkillRequest userSkillRequest) {
        logger.debug("Entering method addUserSkills");

        User user = userRepository.findById(id).get();
        if (user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            List<Skill> inputSkillList = userSkillRequest.getSkillList();
            List<Skill> skillList = inputSkillList.stream()
                    .map(x -> x.builder()
                            .name(x.getName())
                            .user(user)
                            .type(x.getType())
                            .build())
                    .collect(Collectors.toList());
            skillRepository.saveAll(skillList);
            logger.debug("Saved user skill details in database");
            return new ResponseEntity(user.getId(), HttpStatus.OK);

        } catch (Exception exception) {
            logger.debug("Exception occurred in method addUserSkills");
            exception.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
