package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Integer> {
}
