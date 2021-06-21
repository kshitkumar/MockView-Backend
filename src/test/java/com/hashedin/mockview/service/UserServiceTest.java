package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserDto;
import com.hashedin.mockview.exception.DuplicateResourceException;
import com.hashedin.mockview.model.Gender;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void getUserNameForCurrentUser()  {


    }

    @Test
    void userSignUp() throws ParseException, DuplicateResourceException {

        //assuming  user doesn't exist already
        when(userRepository.findByEmailId(any())).thenReturn(null);
        User user = User.builder()
                .firstName("Mahesh")
                .lastName("Kumar")
                .password("123")
                .gender(Gender.MALE)
                .dateOfBirth(Date.valueOf("2002-02-01"))
                .emailId("abc@gmail.com")
                .phoneNumber("9876543210")
                .build();
        when(userRepository.save(any())).thenReturn(user);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");


        User returnedUser = userService.userSignUp(user);
        assertEquals(user.getId(),returnedUser.getId());

    }

    @Test
    void getUserDetails() {
        User user = User.builder()
                .firstName("Mahesh")
                .lastName("Kumar")
                .password("123")
                .gender(Gender.MALE)
                .dateOfBirth(Date.valueOf("2002-02-01"))
                .emailId("abc@gmail.com")
                .phoneNumber("9876543210")
                .build();
        when(userRepository.findByEmailId(any())).thenReturn(user);
        UserDto actualUserDto = userService.getUserDetails(user.getEmailId());
        UserDto expectedUserDto = UserDto.convertToDto(user);
        assertEquals(expectedUserDto.getPhoneNumber(),actualUserDto.getPhoneNumber());

    }
}