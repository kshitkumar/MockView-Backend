package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmailIdAndPassword(String emailId, String password);

    User findByPhoneNumberAndPassword(String mobileNumber, String password);

    User findByEmailId(String emailId);
}
