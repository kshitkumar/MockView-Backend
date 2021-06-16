package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.UserEducation;
import com.hashedin.mockview.model.UserSchoolEducation;
import com.hashedin.mockview.model.UserUniversityEducation;
import com.hashedin.mockview.model.UserWorkExperience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEducationRequest {
    List<UserSchoolEducation> userSchoolEducationsList;
    List<UserUniversityEducation> userUniversityEducationList;

}
