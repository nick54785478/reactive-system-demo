//package com.example.demo.example.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import com.example.demo.example.handler.HelloHandler;
//
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class HelloRouteConfig {
//
//	/**
//	 * 用於定義路由函數及構建服務器響應 RouterFunction<ServerResponse> 。
//	 */
//	@Bean
//	public RouterFunction<ServerResponse> helloRoute() {
//		return RouterFunctions.route(RequestPredicates.GET("/api/v1/hello/{name}"), request -> {
//			String name = request.pathVariable("name");
//			return ServerResponse.ok().body(Mono.just("Hello " + name + "!"), String.class);
//		});
//	}
//	
//	@Bean
//	public RouterFunction<ServerResponse> route(HelloHandler handler) {
//		return RouterFunctions.route(
//				RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
//				handler::hello);
//	}
//}
