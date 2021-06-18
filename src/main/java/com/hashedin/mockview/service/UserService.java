package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.*;
import com.hashedin.mockview.model.*;
import com.hashedin.mockview.repository.*;
import com.hashedin.mockview.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    JwtUtil jwtUtil;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String getUserNameForCurrentUser() {
        logger.debug("Using Security Context Holder for finding username from jwt token");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userName = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public User userSignUp(User inputUser) {

        logger.debug("Entering userSignUp method");
        try {
            User user = userRepository.save(inputUser);
            logger.debug("Saved user {} ", user);
            return user;

        } catch (Exception ex) {
            logger.debug("Exception occurred while saving");
            return new User();
        }


    }

    public String addUserDetails(String userName, UserProfile inputUserProfile) {
        logger.debug("Entering updateUserDetails Method");
        String jwt =null;
        try {
            User savedUser = userRepository.findByEmailId(userName);

            if (savedUser != null) {
                UserProfile userProfile = UserProfile.builder()
                        .user(savedUser)
                        .city(inputUserProfile.getCity())
                        .country(inputUserProfile.getCountry())
                        .state(inputUserProfile.getState())
                        .pinCode(inputUserProfile.getPinCode())
                        .build();
                userProfileRepository.save(userProfile);
                 jwt = generateJwtTokenForCurrentUser(userName);
                return jwt;
            }


        } catch (Exception ex) {
            logger.error("Exception occurred {}", ex.toString());
            ex.printStackTrace();
        }
        return jwt;
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

    public String addEducationDetails(String userName, UserEducationRequest userEducationRequest) {

        logger.debug("Entering addEducationDetails method");
        User user = userRepository.findByEmailId(userName);
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

        return generateJwtTokenForCurrentUser(user.getEmailId());



    }

    public String addUserExperienceDetails(String userName, UserExperienceRequest userExperienceRequest) {
        logger.debug("Entering addUserExperienceDetails");
        User user = userRepository.findByEmailId(userName);
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
            logger.debug("User experience details successfully inserted in database ");
            return generateJwtTokenForCurrentUser(user.getEmailId());


        } catch (Exception exception) {
            logger.error("Exception occurred" + exception.toString());
            exception.printStackTrace();
        }
        return null;

    }

    public String addUserSkills(String userName, UserSkillRequest userSkillRequest) {
        logger.debug("Entering method addUserSkills");

        User user = userRepository.findByEmailId(userName);
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
            return generateJwtTokenForCurrentUser(user.getEmailId());


        } catch (Exception exception) {
            logger.debug("Exception occurred in method addUserSkills");
            exception.printStackTrace();
        }
        return null;
    }

    public String createAuthenticationToken(UserInputRequest userInputRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userInputRequest.getEmailId(),
                            userInputRequest.getPassword()));
        } catch (BadCredentialsException exception) {
            throw new Exception("Incorrect Username or Password", exception);
        }
        return  generateJwtTokenForCurrentUser(userInputRequest.getEmailId());

    }

    private String generateJwtTokenForCurrentUser(String emailId) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(emailId);
        final String jwt = jwtUtil.generateToken(userDetails);
        return jwt;
    }

//    public ResponseEntity getUserDetails(String userName) {
//
//        User user = userRepository.findByEmailId(userName);
//        if (user == null) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//        UserProfile userProfile =user.getUserProfile();
//        List<UserEducation> userEducationList =user.getUserEducationList();
//        List<UserWorkExperience> userWorkExperienceList =user.getUserWorkExperience();
//        List<Skill> skill = user.getSkillList();
//
//        UserProfile outputUserProfile =UserProfile.builder()
//                .city(userProfile.getCity())
//                .country(userProfile.getCountry())
//                .pinCode(userProfile.getPinCode())
//                .state(userProfile.getState())
//                .build();
//        userEducationList.stream()
//                .map(x->x.builder())
//
//        UserDetailResponse userDetailResponse =UserDetailResponse.builder()
//                .userProfile(outputUserProfile)
//
//
//
//    }
}
