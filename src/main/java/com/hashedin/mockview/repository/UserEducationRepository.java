package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEducationRepository extends JpaRepository<UserEducation,Integer> {
    List<UserEducation> findByUser(User user);
}
