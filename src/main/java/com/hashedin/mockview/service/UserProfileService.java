package com.hashedin.mockview.service;

import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.repository.UserProfileRepository;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProfileService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    public void addUserDetails(User user, UserProfile inputUserProfile) throws ResourceNotFoundException {
        log.debug("Entering updateUserDetails Method");

        if (user != null) {
            inputUserProfile.setUser(user);
            userProfileRepository.save(inputUserProfile);
        }

    }

    public UserProfile findProfileDetails(User user) {
        log.debug("Entering findUserProfileDetails method");
        return userProfileRepository.findByUser(user);

    }
}
