package com.hashedin.mockview;

import com.hashedin.mockview.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockViewApplication {

	private static final Logger logger= LoggerFactory.getLogger(MockViewApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MockViewApplication.class, args);
	}

}
