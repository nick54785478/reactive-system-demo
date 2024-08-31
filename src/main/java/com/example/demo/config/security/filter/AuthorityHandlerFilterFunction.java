package com.example.demo.config.security.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.config.security.JwtConstants;
import com.example.demo.exception.response.BaseExceptionResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * 用於定義 角色允許請求的過濾器。
 */
@Slf4j
public class AuthorityHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
		log.info("進入 AuthorityHandlerFilterFunction，進行角色權限檢核");
		// 從 request 中取得 使用者資訊
		String username = (String) request.attributes().get(JwtConstants.JWT_CLAIMS_KEY_USER.getValue());
		List<String> roleList = (List<String>) request.attributes()
				.getOrDefault(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), new ArrayList<>());
		// 從 request 中取得 Http Method
		HttpMethod method = request.method();
		// 從 request 中取得 uri
		String path = request.path();
		log.info("HttpMethod:{} path: {}, username:{}, roleList:{}", method, path, username, roleList);

		// ROOT 權限直接放行
		if (roleList.contains("ADMIN")) {
			return next.handle(request).contextWrite(Context.of(JwtConstants.JWT_CLAIMS_KEY_USER.getValue(), username,
					JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), roleList));
		}

		// 註冊 放行
		if (path.contains("/users/register") && (method.equals(HttpMethod.POST))) {
			return next.handle(request);
		}

		// 查詢 和 新增、修改(註冊)請求直接放行
		if (path.contains("/users") && (!method.equals(HttpMethod.DELETE))) {
			// 設置上下文
			return next.handle(request).contextWrite(Context.of(JwtConstants.JWT_CLAIMS_KEY_USER.getValue(), username,
					JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), roleList));
		}

		// Data Owner 可進行刪除使用者資料
		if (path.contains("/users") && method.equals(HttpMethod.DELETE) && (roleList.contains("DATA_OWNER"))) {
			return next.handle(request);
		}

		// DATA_OWNER 可以賦予使用者角色權限
		if (path.contains("/auth") && (roleList.contains("DATA_OWNER"))) {
			return next.handle(request);
		}

		return ServerResponse.status(HttpStatus.FORBIDDEN).bodyValue(new BaseExceptionResponse(403, "權限不足，不允許此操作"));

	}

}
