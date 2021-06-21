package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.UserProfile;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private UserEducationRequest userEducationRequest;
    private UserExperienceRequest userExperienceRequest;
    private UserSkillRequest userSkillRequest;
    private UserProfile userProfile;
    private UserAwardRequest userAwardRequest;
}
