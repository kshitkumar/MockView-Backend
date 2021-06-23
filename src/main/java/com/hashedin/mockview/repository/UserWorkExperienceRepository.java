package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.Industry;
import com.hashedin.mockview.model.Position;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserWorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWorkExperienceRepository extends JpaRepository<UserWorkExperience, Integer> {
    List<UserWorkExperience> findByUser(User user);

    List<UserWorkExperience> findByIndustryAndPositionAndUserIn(Industry industry, Position position, List<User> userList);

    List<UserWorkExperience> findByIndustryAndUserIn(Industry industry, List<User> userList);

    List<UserWorkExperience> findByIndustryAndCompanyNameAndUserIn(Industry industry, String company, List<User> userList);

    List<UserWorkExperience> findByIndustryAndCompanyNameAndPositionAndUserIn(Industry industry, String company, Position position, List<User> userList);
}
