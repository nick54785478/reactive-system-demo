package com.example.demo.example.client.user;

import java.util.concurrent.CountDownLatch;

import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.iface.dto.CreateUserResource;

import reactor.core.publisher.Mono;

/**
 * 模擬 新增請求
 */
public class CreateUserClient {

	public static void main(String[] args) throws InterruptedException {
		WebClient client = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();

		/*
		 * 在 Java 中，使用 WebClient 發送請求並訂閱 Mono 時，有時會出現無法打印結果的情況，這通常是因為主線程在異步操作完成之前就結束了。
		 * 使用 CountDownLatch 讓主線程等待所有異步操作完成 (測試用)。 每當一個 Mono 完成時，countDown() 會被調用，減少計數器。
		 * 主線程會在 latch.await() 處等待，直到計數器為 0，這表示所有異步操作都已完成。
		 */
		CountDownLatch latch = new CountDownLatch(1);

		// 新增 請求
		CreateUserResource resource = new CreateUserResource(null, "Nick", "nick@example.com", "nick54785478",
				"password123", "新北市淡水區北新路182巷21弄8號");
		Mono<String> resultForAdd = client.post().uri("/users/register").bodyValue(resource).retrieve() // 準備檢索響應。這一步配置了請求以便後續可以獲取到響應。
				.bodyToMono(String.class); // 將響應體解析為 Mono<String>。
		resultForAdd.subscribe(response -> System.out.println("Response Data For Creating User : " + response));

		// 等待所有訂閱完成
		latch.await();
	}
}
