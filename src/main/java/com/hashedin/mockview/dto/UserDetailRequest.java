package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.UserProfile;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailRequest {
    private UserEducationRequest userEducationRequest;
    private UserExperienceRequest userExperienceRequest;
    private UserSkillRequest userSkillRequest;
    private UserProfile userProfile;

}
