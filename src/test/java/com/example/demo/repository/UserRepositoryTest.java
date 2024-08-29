package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.infra.repository.UserRepository;

import reactor.core.publisher.Flux;

@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Flux<UserInfo> all = userRepository.findAll();
		assertTrue(!Objects.isNull(all) );
	}

}
