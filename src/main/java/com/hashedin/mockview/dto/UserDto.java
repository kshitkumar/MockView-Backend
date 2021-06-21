package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.Gender;
import com.hashedin.mockview.model.User;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String emailId;
    private String phoneNumber;
    private String jwt;
    private Boolean profileComplete;

   public static UserDto convertToDto(User user)
    {
        return UserDto.builder().id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .emailId(user.getEmailId())
                .phoneNumber(user.getPhoneNumber())
                .profileComplete(user.getProfileComplete())
                .build();
    }

}
