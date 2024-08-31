package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.iface.dto.CreateJwTokenResource;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootTest
class ReactiveSystemDemoApplicationTests {

	String token = "";

	@Test
	void login() {
		WebClient client = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();
		CreateJwTokenResource resource = new CreateJwTokenResource("nick123", "password123");

		Mono<String> result = client.post().uri("/login").bodyValue(resource).retrieve() // 準備檢索響應。這一步配置了請求以便後續可以獲取到響應。
				.bodyToMono(String.class); // 將響應體解析為 Mono<String>。
		result.doOnSuccess(response -> {
			log.info("token:{}", token);
			assertNotEquals("", response);
		});

	}

	@Test
	void contextLoads() {

	}

}
