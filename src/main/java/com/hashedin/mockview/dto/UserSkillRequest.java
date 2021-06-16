package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.Skill;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSkillRequest {
    private List<Skill> skillList;
}
