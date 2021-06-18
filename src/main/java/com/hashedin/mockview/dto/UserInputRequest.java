package com.hashedin.mockview.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInputRequest {
    private String emailId;
    private String password;


}
