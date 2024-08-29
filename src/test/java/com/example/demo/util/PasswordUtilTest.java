package com.example.demo.util;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PasswordUtilTest {

	private String encodedTarget = "";
	
	@BeforeEach
	void setUp() throws Exception {
		String target = "password123";
		this.encodedTarget = PasswordUtil.encode(target);
	}

	@Test
	void testEncode() {
		String target = "system123";
		this.encodedTarget = PasswordUtil.encode(target);
		System.out.println("encodedTarget: "+encodedTarget);
		assertNotEquals("", encodedTarget);

	}

	@Test
	void testCheckPassword() {
		String target = "password123";
		assertTrue(PasswordUtil.checkPassword(target, encodedTarget));
	}

}
