//package com.example.demo.example.handler;
//
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import com.example.demo.example.Hello;
//
//import reactor.core.publisher.Mono;
//
///**
// * Hello Handler
// * */
//@Component
//public class HelloHandler {
//
//	/**
//	 * 
//	 * */
//	public Mono<ServerResponse> hello(ServerRequest request) {
//		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
//				.body(BodyInserters.fromValue(new Hello("Hello, Spring!")));
//	}
//}
