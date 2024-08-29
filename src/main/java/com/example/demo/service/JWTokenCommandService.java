package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.JWTokenService;
import com.example.demo.domain.share.command.GenerateJWTokenCommand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class JWTokenCommandService {

	JWTokenService jwTokenService;

	/**
	 * 建立 JWToken
	 * 
	 * @return JWToken
	 */
	public Mono<String> generateJWToken(GenerateJWTokenCommand command) {
		return jwTokenService.generateToken(command);
	}

}
