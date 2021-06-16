package com.hashedin.mockview.service;

import com.hashedin.mockview.controller.UserController;
import com.hashedin.mockview.dto.UserEducationRequest;
import com.hashedin.mockview.dto.UserInputRequest;
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
    UserSchoolEducationRepository userSchoolEducationRepository;
    @Autowired
    UserUniversityEducationRepository userUniversityEducationRepository;

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
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

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

    public ResponseEntity addEducationDetails(Integer id,UserEducationRequest userEducationRequest) {

        logger.debug("Entering addEducationDetails method");
        if (id == null) {
            logger.debug("UserId not found in Inout request");
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        try {
            User user = userRepository.findById(id).get();
            List<UserSchoolEducation> inputUserSchoolEducationList = userEducationRequest.getUserSchoolEducationsList();
            List<UserUniversityEducation> inputUserUniversityEducationList = userEducationRequest.getUserUniversityEducationList();

            UserEducation userEducation = UserEducation.builder()
                    .user(user)
                    .build();
            List<UserSchoolEducation> userSchoolEducationList = inputUserSchoolEducationList.stream().map(x -> x.builder()
                    .className(x.getClassName())
                    .userEducation(userEducation)
                    .country(x.getCountry())
                    .marks(x.getMarks())
                    .year(x.getYear())
                    .schoolName(x.getSchoolName())
                    .build()).collect(Collectors.toList());
            List<UserUniversityEducation> userUniversityEducationList = inputUserUniversityEducationList.stream()
                    .map(x -> x.builder()
                            .userEducation(userEducation)
                            .cgpa(x.getCgpa())
                            .country(x.getCountry())
                            .degreeName(x.getDegreeName())
                            .universityName(x.getUniversityName())
                            .yearOfGraduation(x.getYearOfGraduation())
                            .stream(x.getStream())
                            .build()
                    ).collect(Collectors.toList());


            userEducation.setUserSchoolEducation(userSchoolEducationList);
            userEducation.setUserUniversityEducations(userUniversityEducationList);
            userEducationRepository.save(userEducation);

        }
        catch(Exception ex)
        {
            logger.error("Error while saving User Education details");
            logger.debug(ex.toString());
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(id, HttpStatus.OK);


    }

}
