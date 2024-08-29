package com.example.demo.iface.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.domain.share.command.GenerateJWTokenCommand;
import com.example.demo.iface.dto.CreatedJwTokenResource;
import com.example.demo.service.JWTokenCommandService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@NoArgsConstructor
public class LoginHandler {

	@Autowired
	private JWTokenCommandService jwTokenCommandService;

	/**
	 * 取得 JWToken
	 */
	public Mono<ServerResponse> getJWToken(ServerRequest request) {
		log.info("[Role Handler] method: POST, path:/api/vi/roles");
		Mono<GenerateJWTokenCommand> loginMono = request.bodyToMono(GenerateJWTokenCommand.class);

		// flatMap 用於處理非同步流中的元素並將其對應到另一個 Publisher。
		return loginMono.flatMap(command -> jwTokenCommandService.generateJWToken(command))
				.flatMap(token -> ServerResponse.ok().bodyValue(new CreatedJwTokenResource(token)));
	}

}
