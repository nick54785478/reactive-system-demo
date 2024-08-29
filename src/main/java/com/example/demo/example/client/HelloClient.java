//package com.example.demo.example.client;
//
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import com.example.demo.example.Hello;
//
//import reactor.core.publisher.Mono;
//
//@Component 
//public class HelloClient {
//	private final WebClient client; 
//	  public HelloClient(WebClient.Builder builder) { 
//	    this.client = builder.baseUrl("http://localhost:8080").build(); 
//	  } 
//	  public Mono<String> getMessage() { 
//	    return this.client 
//	        .get() 
//	        .uri("/hello") 
//	        .accept(MediaType.APPLICATION_JSON) 
//	        .retrieve() 
//	        .bodyToMono(Hello.class) 
//	        .map(Hello::getMessage); 
//	  } 
//}
