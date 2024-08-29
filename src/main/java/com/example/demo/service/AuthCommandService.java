package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.auth.command.CreateAuthCommand;
import com.example.demo.domain.service.AuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class AuthCommandService {

	AuthService authService;

	/**
	 * 賦予權限
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<String> grantAuth(CreateAuthCommand command) {
		return authService.createAuth(command);
	}
}
