package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.domain.user.aggregate.UserInfo;

import reactor.core.publisher.Flux;

@SpringBootTest
public class IntergrationTest {

	@Test
	void test() throws InterruptedException {
		System.out.println("test");
		WebClient client = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();
		/*
		 * 在 Java 中，使用 WebClient 發送請求並訂閱 Mono 時，有時會出現無法打印結果的情況，這通常是因為主線程在異步操作完成之前就結束了。
		 * 使用 CountDownLatch 讓主線程等待所有異步操作完成 (測試用)。 每當一個 Mono 完成時，countDown() 會被調用，減少計數器。
		 * 主線程會在 latch.await() 處等待，直到計數器為 0，這表示所有異步操作都已完成。
		 */
//		CountDownLatch latch = new CountDownLatch(1);

		// 查詢 請求
		// 將響應體解析為 Mono<String>。
		Flux<UserInfo> userFlux = client.get().uri("/user/userList").retrieve().bodyToFlux(UserInfo.class);
		userFlux.collectList().subscribe(users -> {
			System.out.println("Response from /userList:");
			users.forEach(System.out::println);
		}, error -> System.err.println("Error: " + error.getMessage()));
//		// 等待所有訂閱完成
//		latch.await();

	}
}
