package com.example.demo.iface.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.application.service.AuthCommandService;
import com.example.demo.application.service.JwTokenCommandService;
import com.example.demo.domain.auth.command.CreateAuthCommand;
import com.example.demo.iface.dto.ValidateTokenCommand;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthHandler {

	private final AuthCommandService authCommandService;
	private final JwTokenCommandService jwTokenCommandService;

	public AuthHandler(AuthCommandService authCommandService, JwTokenCommandService jwTokenCommandService) {
		this.authCommandService = authCommandService;
		this.jwTokenCommandService = jwTokenCommandService;
	}

	/**
	 * 賦予使用者權限
	 */
	public Mono<ServerResponse> grantAuth(ServerRequest request) {
		log.info("[Auth Handler] method: POST, path:/api/vi/auth");
		Mono<CreateAuthCommand> createMono = request.bodyToMono(CreateAuthCommand.class);

		// flatMap 用於處理非同步流中的元素並將其對應到另一個 Publisher。
		return createMono.flatMap(command -> authCommandService.grantAuth(command))
				.flatMap(e -> ServerResponse.ok().bodyValue(e));
	}

	/**
	 * 驗證 Token
	 */
	public Mono<ServerResponse> validateToken(ServerRequest request) {
		log.info("[Auth Handler] method: POST, path:/api/v1/auth/validate");
		// 改從 Header 拿 Token
	    String token = request.headers().firstHeader("Authorization");
	    if (token != null && token.startsWith("Bearer ")) {
	        token = token.substring(7);
	    }

	    log.info("[Auth Handler] 拿到 Header Token: {}", token);

	    ValidateTokenCommand command = new ValidateTokenCommand(token);
	    return jwTokenCommandService.validateToken(command)
	            .flatMap(resource -> ServerResponse.ok().bodyValue(resource))
	            .switchIfEmpty(ServerResponse.notFound().build());
	}

}
