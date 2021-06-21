package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserWorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWorkExperienceRepository extends JpaRepository<UserWorkExperience,Integer> {
    List<UserWorkExperience> findByUser(User user);
}
