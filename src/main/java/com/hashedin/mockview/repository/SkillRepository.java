package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.Skill;
import com.hashedin.mockview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill,Integer> {
    List<Skill> findByUser(User user);
}
