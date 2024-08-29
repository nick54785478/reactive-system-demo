//package com.example.demo.iface.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.domain.share.UserInfoData;
//import com.example.demo.domain.user.command.CreateUserCommand;
//import com.example.demo.domain.user.command.UpdateUserCommand;
//import com.example.demo.iface.dto.CreateUserResource;
//import com.example.demo.iface.dto.UpdateUserResource;
//import com.example.demo.iface.dto.UserInfoResource;
//import com.example.demo.service.UserCommandService;
//import com.example.demo.service.UserQueryService;
//import com.example.demo.util.BaseDataTransformer;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * Controller ，此處改以 Route & Handler 處理
// * */
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/users")
//public class UserController {
//	
//	@Autowired
//	private UserCommandService userCommandService;
//	@Autowired
//	private UserQueryService userQueryService;
//
//	@GetMapping("/{id}")
//	public Mono<UserInfoResource> getUser(@PathVariable Long id) {
//		// 防腐處理 userData -> resource
//		return userQueryService.getUserById(id).map(e -> BaseDataTransformer.transformData(e, UserInfoResource.class));
//	}
//
//	@GetMapping("")
//	@ResponseStatus(HttpStatus.OK)
//	public Flux<UserInfoResource> getUserList() {
//		Flux<UserInfoData> userList = userQueryService.getUserList();
//		// 防腐處理 userData -> resource
//		return userList.map(e -> BaseDataTransformer.transformData(e, UserInfoResource.class));
//	}
//
//	@PostMapping
//    @ResponseStatus(HttpStatus.CREATED) // 設置 HTTP 狀態碼為 201 Created
//	public Mono<String> createUser(@RequestBody CreateUserResource resource) {
//		// 防腐處理 resource -> command
//		CreateUserCommand command = BaseDataTransformer.transformData(resource, CreateUserCommand.class);
//		return userCommandService.createUserInfo(command);
//	}
//
//	@PutMapping("")
//	public Mono<String> updateUser(@RequestBody UpdateUserResource resource) {
//		// 防腐處理 resource -> command
//		UpdateUserCommand command = BaseDataTransformer.transformData(resource, UpdateUserCommand.class);
//		return userCommandService.updateUserInfo(command);
//	}
//
//	@DeleteMapping("/{id}")
//	public Mono<String> deleteUser(@PathVariable Long id) {
//		return userCommandService.deleteUserInfoById(id);
//	}
//
//}
