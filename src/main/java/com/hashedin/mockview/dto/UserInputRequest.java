package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInputRequest {
    private String emailId;
    private String phoneNumber;
    private String password;


}
