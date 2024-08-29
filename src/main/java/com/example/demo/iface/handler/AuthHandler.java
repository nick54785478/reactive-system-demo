package com.example.demo.iface.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.domain.auth.command.CreateAuthCommand;
import com.example.demo.service.AuthCommandService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@NoArgsConstructor
public class AuthHandler {

	@Autowired
	AuthCommandService authCommandService;

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
}
