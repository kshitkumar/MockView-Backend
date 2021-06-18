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

    public void addUserDetails(Integer id, UserProfile inputUserProfile) throws ResourceNotFoundException {
        log.debug("Entering updateUserDetails Method");
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));

        if (savedUser != null) {
            UserProfile userProfile = UserProfile.builder()
                    .user(savedUser)
                    .city(inputUserProfile.getCity())
                    .country(inputUserProfile.getCountry())
                    .state(inputUserProfile.getState())
                    .pinCode(inputUserProfile.getPinCode())
                    .build();
            userProfileRepository.save(userProfile);
        }
    }
}
