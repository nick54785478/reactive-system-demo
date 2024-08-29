package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;

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
	public Mono<String> createUserInfo(CreateUserCommand command) {
		return userService.createUser(command);
	}

	/**
	 * 更新 使用者資料
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<String> updateUserInfo(UpdateUserCommand command) {
		return userService.updateUser(command);
	}

	/**
	 * 刪除 使用者資料
	 * 
	 * @param id
	 * @return 成功訊息
	 */
	public Mono<String> deleteUserInfoById(Long id) {
		return userService.deleteUser(id);
	}

}
