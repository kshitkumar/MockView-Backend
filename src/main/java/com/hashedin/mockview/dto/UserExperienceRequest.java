package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.UserWorkExperience;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserExperienceRequest {

    private List<UserWorkExperience> userWorkExperienceList;
}
