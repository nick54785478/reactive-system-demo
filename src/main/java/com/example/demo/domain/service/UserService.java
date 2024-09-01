package com.example.demo.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.AuthRepository;
import com.example.demo.infra.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Domain Service
 */
@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthRepository authRepository;

	/**
	 * 根據使用者ID 查詢使用者
	 * 
	 * @param id - 使用者代號
	 * @return Mono<User>
	 */
	public Mono<UserInfo> getUserById(Long id) {
		return userRepository.findById(id);
	}

	/**
	 * 取得使用者清單
	 * 
	 * @return Flux<User>
	 */
	public Flux<UserInfo> getUserList() {
		return userRepository.findAll();
	}

	/**
	 * 新增使用者資料
	 * 
	 * @param command
	 * @return Mono<User>
	 */
	public Mono<String> createUser(CreateUserCommand command) {
		Mono<UserInfo> userMono = userRepository.findByUsername(command.getUsername());
		return userMono.hasElement().flatMap(hasElements -> {
			if (hasElements) {
				log.error("指定帳戶已存在，註冊失敗");
				throw new ValidationException(409, "指定帳戶已存在，註冊失敗");
			} else {
				UserInfo userInfo = new UserInfo();
				userInfo.create(command);
				return userRepository.save(userInfo).map(e -> "成功新增一筆資料，id:" + e.getId());
			}
		});

	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 * @return Mono<Integer> 更新資料筆數
	 */
	public Mono<String> updateUser(UpdateUserCommand command) {
		return userRepository.findById(command.getId()).flatMap(user -> {
			user.update(command);
			// 保存用戶
			return userRepository.save(user);
		}).map(e -> "成功更新一筆資料, id:" + command.getId());
	}

	/**
	 * 刪除使用者資料
	 * 
	 * @param id - 使用者代號
	 * @return Mono<UserInfo>
	 */
	public Mono<String> deleteUser(Long id) {
		return userRepository.findById(id).flatMap(user -> {
			// 更新 activeFlag = "N"
			user.delete();
			// 保存用戶
			return userRepository.save(user).flatMap(e -> {
				// 用戶失效後將其相關權限一併移除
				return authRepository.deleteByUserId(e.getId())
						.thenReturn("成功刪除一筆資料");
			});
		});
	}

}
