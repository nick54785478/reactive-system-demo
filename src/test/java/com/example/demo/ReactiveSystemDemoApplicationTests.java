package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.iface.dto.CreateJwTokenResource;
import com.example.demo.iface.dto.JwTokenCreatedResource;
import com.example.demo.iface.dto.UserInfoResource;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@WebFluxTest
class ReactiveSystemDemoApplicationTests {

	CountDownLatch latch = null;
	static String token = "";
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// 取得 JWToken
		WebClient client = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();
		CreateJwTokenResource resource = new CreateJwTokenResource("nick123", "password123");
		Mono<JwTokenCreatedResource> result = client.post().uri("/login").bodyValue(resource).retrieve() // 準備檢索響應。這一步配置了請求以便後續可以獲取到響應。
				.bodyToMono(JwTokenCreatedResource.class); // 將響應體解析為 Mono<CreatedJwTokenResource>。
		
		result.doOnSuccess(response -> {
			token = response.getToken();			
		}).subscribe();
	}

	@BeforeEach
	void setUp() throws Exception {
		/*
		 * 在 Java 中，使用 WebClient 發送請求並訂閱 Mono 時，有時會出現無法打印結果的情況，這通常是因為主線程在異步操作完成之前就結束了。
		 * 使用 CountDownLatch 讓主線程等待所有異步操作完成 (測試用)。 每當一個 Mono 完成時，countDown() 會被調用，減少計數器。
		 * 主線程會在 latch.await() 處等待，直到計數器為 0，這表示所有異步操作都已完成。
		 */
		this.latch = new CountDownLatch(1);
		
	}

	/**
	 * 登入取得 JWToken
	 * */
	@Test
	void login() throws InterruptedException {
		// 模擬一個線程
		Thread thread = new Thread(() -> {
			WebClient client = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();
			CreateJwTokenResource resource = new CreateJwTokenResource("nick123", "password123");
			Mono<JwTokenCreatedResource> result = client.post().uri("/login").bodyValue(resource).retrieve() // 準備檢索響應。這一步配置了請求以便後續可以獲取到響應。
					.bodyToMono(JwTokenCreatedResource.class); // 將響應體解析為 Mono<CreatedJwTokenResource>。
			
			result.doOnSuccess(response -> {
				log.info("Login successfully and get token:{}", response.getToken());
				assertNotEquals("", response.getToken());
			}).subscribe();
		});
		
		thread.start();
		
		// 等待主線程完成
		try {
            log.info("Main thread waiting...");
            // 稍等 3 秒。
            latch.await(3, TimeUnit.SECONDS); // 等待 latch 被釋放
			log.info("Main thread resumed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

	}
	
	/**
	 * 取得使用者清單
	 * */
	@Test
	void testGetUserList() {
		// 模擬一個線程
		Thread thread = new Thread(() -> {
			WebClient client = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();
			log.debug("Token: "+token);
			
			// 查詢 請求
			Flux<UserInfoResource> userFlux = client.get().uri("/users")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
					.retrieve().bodyToFlux(UserInfoResource.class);
			// 遍歷 List & 印出裡面的元素
			userFlux.collectList().doOnSuccess(response -> {
				System.out.println("Response from /users,  userList:");
				response.forEach(System.out::println);
				assertFalse(response.isEmpty());
			}).subscribe();
		});
		
		thread.start();

		
		// 等待主線程完成
		try {
            log.info("Main thread waiting...");
            latch.await(3, TimeUnit.SECONDS); // 稍等 3 秒，等待 latch 被釋放
			log.info("Main thread resumed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
					
	}

	@Test
	void contextLoads() {

	}

	@AfterEach
	void tearDown() throws Exception {
		latch.countDown();
	}

}
