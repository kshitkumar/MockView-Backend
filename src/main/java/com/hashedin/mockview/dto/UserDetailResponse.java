package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.UserEducation;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.model.UserWorkExperience;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponse {

    List<UserWorkExperience> userWorkExperienceList;
    List<UserEducation> userEducationList;
    List<UserProfile> userProfileList;
    UserProfile userProfile;
}
