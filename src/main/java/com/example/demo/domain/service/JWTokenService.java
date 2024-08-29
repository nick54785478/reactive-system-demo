package com.example.demo.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.auth.aggregate.Auth;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.command.GenerateJWTokenCommand;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.AuthRepository;
import com.example.demo.infra.repository.RoleRepository;
import com.example.demo.infra.repository.UserRepository;
import com.example.demo.util.JwtTokenUtil;
import com.example.demo.util.PasswordUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class JWTokenService {

	AuthRepository authRepository;
	UserRepository userRepository;
	RoleRepository roleRepository;
	JwtTokenUtil jwtTokenUtil;
	
	/**
	 * 建立 JWToken
	 * 
	 * @param command 內含使用者帳號 & 密碼
	 * @return JWToken
	 */
	public Mono<String> generateToken(GenerateJWTokenCommand command) {

		// 取出使用者資料
		Mono<UserInfo> userMono = userRepository.findByUsername(command.getUsername());
		
		// 取得角色名稱清單
		Mono<List<String>> roleList = userMono.flatMap(userInfo -> {
			// 驗證密碼
			var isMatch = PasswordUtil.checkPassword(command.getPassword(), userInfo.getPassword());
			if (!isMatch) {
				throw new ValidationException(401, "Password does not match");// 比對失敗
			}
			return Mono.just(userInfo); // 比對成功，返回 userInfo
		}).map(UserInfo::getId)
				// 取得角色 ID 列表
				.flatMapMany(userId -> authRepository.findByUserIdAndActiveFlag(userId, "Y").map(Auth::getRoleId))
				.collectList() // 將 Flux<Long> 轉為 List<String>
				.flatMapMany(ids -> roleRepository.findByIdInAndActiveFlag(ids, "Y").map(RoleInfo::getName))
				.collectList();// 取得角色名稱清單

		log.info("roleList: {}", roleList);

		// 透過 flatMap 處理資料
		return roleList.flatMap(e -> {
			// 透過 Mono.just 轉 String 為 Mono<String>
			return Mono.just(jwtTokenUtil.generateToken(command.getUsername(), e));
		});
	}
	
	
}
