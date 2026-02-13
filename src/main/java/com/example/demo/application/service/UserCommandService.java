package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.iface.dto.UserCreatedResource;
import com.example.demo.iface.dto.UserDeletedResource;
import com.example.demo.iface.dto.UserUpdatedResource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Application Service
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class UserCommandService {

	UserService userService;

	/**
	 * 新增 使用者資料
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<UserCreatedResource> createUserInfo(CreateUserCommand command) {
		return userService.createUser(command).map(message -> new UserCreatedResource(201, message));
	}

	/**
	 * 更新 使用者資料
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<UserUpdatedResource> updateUserInfo(UpdateUserCommand command) {
		return userService.updateUser(command).map(message -> new UserUpdatedResource(200, message));
	}

	/**
	 * 刪除 使用者資料
	 * 
	 * @param id
	 * @return 成功訊息
	 */
	public Mono<UserDeletedResource> deleteUserInfoById(Long id) {
		return userService.deleteUser(id).map(message -> new UserDeletedResource(200, message));
	}

}
