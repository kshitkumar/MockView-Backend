package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserDto;
import com.hashedin.mockview.exception.DuplicateResourceException;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public String getUserNameForCurrentUser() {
        log.debug("Using Security Context Holder for finding username from jwt token");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userName = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public User userSignUp(User inputUser) throws DuplicateResourceException {

        User userFound = userRepository.findByEmailId(inputUser.getEmailId());
        if (userFound != null)
            throw new DuplicateResourceException("User already exists for mailId " + inputUser.getEmailId());
        User user = User.builder()
                .dateOfBirth(inputUser.getDateOfBirth())
                .emailId(inputUser.getEmailId())
                .firstName(inputUser.getFirstName())
                .lastName(inputUser.getLastName())
                .gender(inputUser.getGender())
                .phoneNumber(inputUser.getPhoneNumber())
                .password(passwordEncoder.encode(inputUser.getPassword()))
                .profileComplete(Boolean.FALSE)
                .build();
        return userRepository.save(user);


    }

    public UserDto getUserDetails(String emailId) {
        User user = userRepository.findByEmailId(emailId);
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .emailId(user.getEmailId())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .profileComplete(user.getProfileComplete())
                .build();
    }


}
