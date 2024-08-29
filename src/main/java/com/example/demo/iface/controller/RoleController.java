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
//import com.example.demo.domain.role.command.CreateRoleCommand;
//import com.example.demo.domain.role.command.UpdateRoleCommand;
//import com.example.demo.domain.share.RoleInfoData;
//import com.example.demo.iface.dto.CreateRoleResource;
//import com.example.demo.iface.dto.RoleInfoResource;
//import com.example.demo.iface.dto.UpdateRoleResource;
//import com.example.demo.service.RoleCommandService;
//import com.example.demo.service.RoleQueryService;
//import com.example.demo.util.BaseDataTransformer;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * Controller ，此處改以 Route & Handler 處理
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/roles")
//public class RoleController {
//
//	@Autowired
//	RoleCommandService roleCommandService;
//	@Autowired
//	RoleQueryService roleQueryService;
//
//	@PostMapping
//    @ResponseStatus(HttpStatus.CREATED) // 設置 HTTP 狀態碼為 201 Created
//	public Mono<String> createrole(@RequestBody CreateRoleResource resource) {
//		// 防腐處理 resource -> command
//		CreateRoleCommand command = BaseDataTransformer.transformData(resource, CreateRoleCommand.class);
//		return roleCommandService.createRoleInfo(command);
//	}
//	
//	@GetMapping("/{id}")
//	public Mono<RoleInfoResource> getUser(@PathVariable Long id) {
//		// 防腐處理 userData -> resource
//		return roleQueryService.getRoleById(id).map(e -> BaseDataTransformer.transformData(e, RoleInfoResource.class));
//	}
//
//	@GetMapping("")
//	@ResponseStatus(HttpStatus.OK)
//	public Flux<RoleInfoResource> getUserList() {
//		Flux<RoleInfoData> roleList = roleQueryService.getRoleList();
//		// 防腐處理 userData -> resource
//		return roleList.map(e -> BaseDataTransformer.transformData(e, RoleInfoResource.class));
//	}
//
//	@PutMapping("")
//	public Mono<String> updaterole(@RequestBody UpdateRoleResource resource) {
//		// 防腐處理 resource -> command
//		UpdateRoleCommand command = BaseDataTransformer.transformData(resource, UpdateRoleCommand.class);
//		return roleCommandService.updateRoleInfo(command);
//	}
//
//	@DeleteMapping("/{id}")
//	public Mono<String> deleterole(@PathVariable Long id) {
//		return roleCommandService.deleteRoleInfoById(id);
//	}
//
//}
