package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    UserProfile findByUser(User user);
}
