package com.hashedin.mockview;

import com.hashedin.mockview.dto.UserAwardRequest;
import com.hashedin.mockview.dto.UserDetailRequest;
import com.hashedin.mockview.dto.UserDetailResponse;
import com.hashedin.mockview.model.Award;
import com.hashedin.mockview.model.Gender;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserProfile;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;

@SpringBootApplication
@Slf4j
public class MockViewApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MockViewApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		User user = User.builder()
//				.firstName("Mahesh")
//				.lastName("Tripathi")
//				.dateOfBirth(Date.valueOf("1969-01-01"))
//				.gender(Gender.MALE)
//				.emailId("mahesh@gmail.com")
//				.phoneNumber("9876543210").build();
//		userRepository.save(user);
//
//		Award award =
//		UserAwardRequest userAwardRequest =UserAwardRequest.builder()
//				.userAwardList()
//		UserDetailRequest userDetailRequest =UserDetailRequest.builder()
//				.userAwardRequest()

	}
}
