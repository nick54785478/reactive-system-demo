package com.example.demo.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.auth.aggregate.Auth;
import com.example.demo.domain.auth.command.CreateAuthCommand;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.infra.repository.AuthRepository;
import com.example.demo.infra.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

	private AuthRepository authRepository;
	private UserRepository userRepository;

	/**
	 * 建立使用者特定權限
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<String> createAuth(CreateAuthCommand command) {
		Mono<Long> userIdMono = userRepository.findByUsername(command.getUsername()).map(UserInfo::getId);

		return userIdMono.flatMap(userId ->
		// 刪除該使用者所有權限( admin 不能刪除)
		authRepository.deleteByUserIdAndReserveAdmin(userId).thenReturn(userId) // 將 userId 回傳
		).flatMap(userId -> {
			// 遍歷 RoleId 列表，更新 權限資料
			List<Auth> authList = command.getRoleList().stream().map(roleId -> {
				Auth auth = new Auth();
				auth.create(userId, roleId);
				log.info("auth: {}", auth);
				return auth;
			}).collect(Collectors.toList());
			// 成功後回傳訊息
			return authRepository.saveAll(authList).then(Mono.just("成功變更權限資料"));
		});
	}
}
