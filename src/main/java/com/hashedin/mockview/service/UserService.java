package com.hashedin.mockview.service;

import com.hashedin.mockview.controller.UserController;
import com.hashedin.mockview.dto.UserInputRequest;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.repository.UserProfileRepository;
import com.hashedin.mockview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

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

    public ResponseEntity updateUserDetails(Integer id, UserProfile inputUserProfile) {
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
            logger.debug("User details not Found");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception ex) {
            logger.debug("Exception occured {}", ex.toString());
            return new ResponseEntity(ex.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity loginUser(UserInputRequest userInputRequest) {
        logger.debug("Entering loginUser Method");
        //User logging in via Email Id
        if (userInputRequest.getEmailId() != null) {
            User user= userRepository.findByEmailIdAndPassword(userInputRequest.getEmailId(),userInputRequest.getPassword());

            if(user!=null)
            {
                logger.debug("Login Successful via Email Id");
                return new ResponseEntity(HttpStatus.OK);
            }
            else
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else if(userInputRequest.getPhoneNumber()!=null){
            User user= userRepository.findByPhoneNumberAndPassword(userInputRequest.getPhoneNumber(),userInputRequest.getPassword());

            if(user!=null)
            {
                logger.debug("Login Successful via Phone Number");
                return new ResponseEntity(HttpStatus.OK);
            }
            else
                return new ResponseEntity(HttpStatus.BAD_REQUEST);


        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
