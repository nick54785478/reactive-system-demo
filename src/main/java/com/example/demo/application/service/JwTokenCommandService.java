package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.application.port.JwTokenManagerPort;
import com.example.demo.domain.auth.aggregate.Auth;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.command.GenerateJwTokenCommand;
import com.example.demo.iface.dto.TokenValidatedResource;
import com.example.demo.iface.dto.ValidateTokenCommand;
import com.example.demo.infra.exception.exception.ValidationException;
import com.example.demo.infra.repository.AuthRepository;
import com.example.demo.infra.repository.RoleRepository;
import com.example.demo.infra.repository.UserRepository;
import com.example.demo.util.PasswordUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class JwTokenCommandService {

	private JwTokenManagerPort jwTokenManager;
	private UserRepository userRepository;
	private AuthRepository authRepository;
	private RoleRepository roleRepository;

	/**
	 * 授權與建立 Token 的 Use Case
	 * 
	 * @param command
	 * @return token
	 */
	public Mono<String> generateJWToken(GenerateJwTokenCommand command) {
		return userRepository.findByUsername(command.getUsername())
				// 1. 處理找不到使用者的情況
				.switchIfEmpty(Mono.error(new ValidationException(401, "User not found")))
				// 2. 使用 filter 進行密碼檢查
				.filter(userInfo -> PasswordUtil.checkPassword(command.getPassword(), userInfo.getPassword()))
				// 3. 如果 filter 沒過，代表密碼錯了
				.switchIfEmpty(Mono.error(new ValidationException(401, "Password does not match")))
				// 4. 通過後才進入角色查詢與 Token 生成
				.flatMap(userInfo -> getRoleNamesByUserId(userInfo.getId())
						.map(roles -> jwTokenManager.generateToken(command.getUsername(), userInfo.getEmail(), roles)));
	}

	/**
	 * 私有輔助方法：封裝查詢角色的複雜流
	 * 
	 * @param userId 使用者 ID
	 * @return 使用者清單
	 */
	private Mono<List<String>> getRoleNamesByUserId(Long userId) {
		return authRepository.findByUserIdAndActiveFlag(userId, "Y").map(Auth::getRoleId).collectList()
				.flatMapMany(ids -> {
					if (ids.isEmpty()) {
						// 明確告知這是 Flux<RoleInfo> 的空流
						return Flux.<RoleInfo>empty();
					}
					return roleRepository.findByIdInAndActiveFlag(ids, "Y");
				}).map(RoleInfo::getName).collectList();
	}

	/**
	 * 驗證 Token
	 * 
	 * @param command 驗證 Token
	 * @return Token 資訊
	 */
	public Mono<TokenValidatedResource> validateToken(ValidateTokenCommand command) {
		return Mono.just(command.getToken()).map(
				token -> new TokenValidatedResource(jwTokenManager.getEmail(token), jwTokenManager.getRoleList(token)));
	}

}
