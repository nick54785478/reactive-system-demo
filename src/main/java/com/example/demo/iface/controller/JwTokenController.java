//package com.example.demo.iface.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.domain.user.command.CreateUserCommand;
//import com.example.demo.iface.dto.CreateJwTokenResource;
//import com.example.demo.iface.dto.CreatedJwTokenResource;
//import com.example.demo.util.BaseDataTransformer;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/login")
//public class JwTokenController {
//
//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED) // 設置 HTTP 狀態碼為 201 Created
//	public Mono<CreatedJwTokenResource> login(@RequestBody CreateJwTokenResource resource) {
//		
//		
//		// 防腐處理 resource -> command
//		CreateUserCommand command = BaseDataTransformer.transformData(resource, CreateUserCommand.class);
//		return null;
//	}
//}
