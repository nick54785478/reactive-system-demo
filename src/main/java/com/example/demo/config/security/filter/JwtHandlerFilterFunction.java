package com.example.demo.config.security.filter;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.config.security.JwtConstants;
import com.example.demo.exception.response.BaseExceptionResponse;
import com.example.demo.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 用於定義 JWT 處理允許請求的過濾器。
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JwtHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	// HandlerFilterFunction 實作僅適用於基於 Router 的端點
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {

		// 從 request 中取得 Http Method
		HttpMethod method = request.method();
		// 從 request 中取得 uri
		String path = request.path();
		// 註冊放行
		if (path.contains("/users/register") && (method.equals(HttpMethod.POST))) {
			return next.handle(request);
		}
		
		log.info("進入 JwtHandlerFilterFunction，開始檢核 JWToken");

		String authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);

		// 提取 JWT（去掉前面的 "Bearer " 部分）
		String token = authHeader.substring(JwtConstants.JWT_PREFIX.getValue().length());

		try {
			// 驗證 JWT 的有效性
			if (jwtTokenUtil.validateToken(token)) {
				// 將用戶名、角色儲存到當前請求屬性中，以供後續使用。
				// 如果 JWT 有效，從中取得使用者帳號
				String username = jwtTokenUtil.getUsername(token);
				List<String> roleList = jwtTokenUtil.getRoleList(token);
				request.attributes().put(JwtConstants.JWT_CLAIMS_KEY_USER.getValue(), username);
				request.attributes().put(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), roleList);
				log.info("從 JWToken 取得資料，username:{}, roleList:{}", username, roleList);
				return next.handle(request);
			} else {
				return ServerResponse.status(HttpStatus.UNAUTHORIZED)
						.bodyValue(new BaseExceptionResponse(401, "Token 驗證例外拋出"));
			}

		} catch (MalformedJwtException | UnsupportedJwtException e) {
			log.error("Token 驗證例外拋出: ", e);
			return ServerResponse.status(HttpStatus.UNAUTHORIZED)
					.bodyValue(new BaseExceptionResponse(401, "Token 驗證例外拋出"));
		} catch (NullPointerException e) {
			log.error("未攜帶Token，驗證發生錯誤: ", e);
			return ServerResponse.status(HttpStatus.UNAUTHORIZED)
					.bodyValue(new BaseExceptionResponse(401, "未攜帶Token，驗證發生錯誤"));
		} catch (ExpiredJwtException e) {
			log.error("Token已經過期，請求中止", e);
			return ServerResponse.status(HttpStatus.UNAUTHORIZED)
					.bodyValue(new BaseExceptionResponse(401, "Token已經過期，請求中止"));
		} catch (SignatureException e) {
			log.error("Token 驗證不被信任", e);
			return ServerResponse.status(HttpStatus.UNAUTHORIZED)
					.bodyValue(new BaseExceptionResponse(401, "Token 驗證不被信任，請求中止"));
		}

	}

}
