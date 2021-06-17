package com.hashedin.mockview.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationResponse {

    private  String jwt;


}
