package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.UserEducation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEducationRequest {
    List<UserEducation> userEducationList;

}
