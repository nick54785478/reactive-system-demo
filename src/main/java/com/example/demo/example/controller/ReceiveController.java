//package com.example.demo.example.controller;
//
//import java.util.concurrent.TimeUnit;
//import java.util.stream.IntStream;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import com.example.demo.example.Hello;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1")
//public class ReceiveController {
//
//	/**
//	 * 在反應式系統中模仿 MVC 架構的 functional 寫法
//	 * 
//	 */
//	@GetMapping("/hello")
//	public Mono<ServerResponse> hello(ServerRequest request) {
//		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
//				.body(BodyInserters.fromValue(new Hello("Hello, Spring!")));
//	}
//
//	@GetMapping("/hello/{name}")
//	public Mono<String> helloWho(@PathVariable("name") String name) {
//		// Mono.just 用於簡單地返回包含靜態值或計算結果
//		return Mono.just("Hello " + name); // 返回一個包含 Hello, {Name}! 字符串的 Mono。
//	}
//
//	/**
//	 * 使用 flux，用 Stream 返回0-N個元素
//	 */
//	@GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<String> flux() {
//		long timeMillis = System.currentTimeMillis();
//		log.info("webflux() start");
//		// 每秒跑一次，共四次
//		Flux<String> result = Flux.fromStream(IntStream.range(1, 5).mapToObj(I -> {
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			return "flux data—" + I;
//		}));
//		log.info("flux() end use time {}/ms", System.currentTimeMillis() - timeMillis);
//		return result;
//	}
//}
