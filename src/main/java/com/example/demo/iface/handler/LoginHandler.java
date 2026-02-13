package com.example.demo.iface.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.application.service.JwTokenCommandService;
import com.example.demo.domain.share.command.GenerateJwTokenCommand;
import com.example.demo.iface.dto.JwTokenCreatedResource;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoginHandler {

	private final JwTokenCommandService jwTokenCommandService;

	public LoginHandler(JwTokenCommandService jwTokenCommandService) {
		this.jwTokenCommandService = jwTokenCommandService;
	}

	/**
	 * 取得 JWToken
	 */
	public Mono<ServerResponse> getJWToken(ServerRequest request) {
		log.info("[Login Handler] method: POST, path:/api/vi/login");
		Mono<GenerateJwTokenCommand> loginMono = request.bodyToMono(GenerateJwTokenCommand.class);

		// flatMap 用於處理非同步流中的元素並將其對應到另一個 Publisher。
		return loginMono.flatMap(command -> jwTokenCommandService.generateJWToken(command))
				.flatMap(token -> ServerResponse.ok().bodyValue(new JwTokenCreatedResource(token)));
	}

}
