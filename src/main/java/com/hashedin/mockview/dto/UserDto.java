package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.Gender;
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

}
