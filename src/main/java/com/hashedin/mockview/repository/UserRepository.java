package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmailIdAndPassword(String emailId, String password);

    User findByPhoneNumberAndPassword(String mobileNumber, String password);

    User findByEmailId(String emailId);

    @Modifying
    @Query("update User set profileComplete =:status where id =:id")
    void updateProfileStatus(Boolean status,Integer id);

    @Modifying
    @Query("update User set password=:newBcryptPassword where id=:id")
    void updatePassword(String newBcryptPassword, Integer id);
}
