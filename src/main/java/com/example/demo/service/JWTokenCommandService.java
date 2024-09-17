package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.JWTokenService;
import com.example.demo.domain.share.command.GenerateJWTokenCommand;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.UserRepository;
import com.example.demo.util.PasswordUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class JWTokenCommandService {

	private JWTokenService jwTokenService;
	private UserRepository userRepository;

	/**
	 * 建立 JWToken
	 * 
	 * @return JWToken
	 */
	public Mono<String> generateJWToken(GenerateJWTokenCommand command) {

		Mono<UserInfo> userMono = userRepository.findByUsername(command.getUsername());
		
		return userMono.flatMap(userInfo -> {
						
			// 驗證密碼
			var isMatch = PasswordUtil.checkPassword(command.getPassword(), userInfo.getPassword());
			if (!isMatch) {
				log.error("Password does not match");
				throw new ValidationException(401, "Password does not match");// 比對失敗
			}
			return jwTokenService.generateToken(userInfo.getId(), command.getUsername(), userInfo.getEmail());
		});

	}

}
