package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.Award;
import com.hashedin.mockview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award,Integer> {
    List<Award> findByUser(User user);
}
