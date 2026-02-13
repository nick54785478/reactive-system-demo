package com.example.demo.iface.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.application.service.UserCommandService;
import com.example.demo.application.service.UserQueryService;
import com.example.demo.domain.share.dto.UserInfoData;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.iface.dto.UserInfoResource;
import com.example.demo.infra.jwt.shared.constant.JwtConstants;
import com.example.demo.util.BaseDataTransformer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@NoArgsConstructor
public class UserHandler {

	@Autowired
	private UserCommandService userCommandService;
	@Autowired
	private UserQueryService userQueryService;

	/**
	 * 透過 userId 取得 User 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> getUser(ServerRequest request) {
		// 取得 user id
		String userId = request.pathVariable("id");
		log.debug("userId:{}", userId);

		// 上下文需在 Handler 取得，否則會有多個 ContextView
		return Mono.deferContextual(ctx -> {
			// 從上下文取得 使用者資訊。
			String username = ctx.get(JwtConstants.JWT_CLAIMS_KEY_USER.getValue());
			List<String> roleList = ctx.get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
			Mono<UserInfoData> userMono = userQueryService.getUserById(Long.parseLong(userId));
			return userMono.flatMap(userData -> {
				// 只能查自己的資料、若為 ADMIN 或 DATA_OWNER 則放行。
				if ((roleList.isEmpty() && StringUtils.equals(username, userData.getUsername()))
						|| (roleList.contains("ADMIN") || roleList.contains("DATA_OWNER"))) {
					return ServerResponse.ok()
							.bodyValue(BaseDataTransformer.transformData(userData, UserInfoResource.class))
							.switchIfEmpty(ServerResponse.notFound().build());
				}
				return ServerResponse.noContent().build();
			});
		});

	}

	/**
	 * 透過 email 取得 User 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> getUserByEmail(ServerRequest request) {

		// 上下文需在 Handler 取得，否則會有多個 ContextView
		return Mono.deferContextual(ctx -> {
			// 從上下文取得 使用者資訊。
			List<String> roleList = ctx.get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
			String email = request.queryParam("email").orElse("");
			log.info("email:{}", email);

			Mono<UserInfoData> userMono = userQueryService.getUserByEmail(email);

			return userMono.flatMap(userData -> {
				// 以下條件放行:
				// 1. Token 中的 email 相等、
				// 2. ADMIN 或 DATA_OWNER 則放行。
				if ((StringUtils.equals(userData.getEmail(), ctx.get(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue())))
						|| (roleList.contains("ADMIN")) || roleList.contains("DATA_OWNER")) {
					return ServerResponse.ok()
							.bodyValue(BaseDataTransformer.transformData(userData, UserInfoResource.class));
				}

				return ServerResponse.noContent().build();
			});
		});

	}

	/**
	 * 取得 User 資料清單
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> getUserList(ServerRequest request) {

		// 上下文需在 Handler 取得，否則會有多個 ContextView
		return Mono.deferContextual(ctx -> {
			// 從上下文取得 使用者資訊。
			String username = ctx.get(JwtConstants.JWT_CLAIMS_KEY_USER.getValue());
			List<String> roleList = ctx.get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
			Flux<UserInfoData> userList = userQueryService.getUserList();

			// 沒有權限，只能查自己
			if (roleList.isEmpty()) {
				// 過濾自己的使用者資料
				Flux<UserInfoData> filteredList = userList
						.filter(user -> StringUtils.equals(username, user.getUsername()));
				return ServerResponse.ok().body(filteredList, UserInfoResource.class);
			}

			return userList.hasElements().flatMap(hasElements -> {
				if (hasElements) {
					// 有值傳回
					return ServerResponse.ok().body(userList, UserInfoResource.class);
				} else {
					// 無值傳回空 List
					return ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue(new ArrayList<>());
				}
			});
		});
	}

	/**
	 * 新增 User 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> createUser(ServerRequest request) {
		Mono<CreateUserCommand> userMono = request.bodyToMono(CreateUserCommand.class);
		// flatMap 用於處理非同步流中的元素並將其對應到另一個 Publisher。
		return userMono.flatMap(userCommandService::createUserInfo).flatMap(e -> ServerResponse.ok().bodyValue(e));
	}

	/**
	 * 更新 User 資料
	 * 
	 * @param request
	 * @return 響應資料 *
	 */
	public Mono<ServerResponse> updateUser(ServerRequest request) {
		Mono<UpdateUserCommand> userMono = request.bodyToMono(UpdateUserCommand.class);
		return userMono.flatMap(userCommandService::updateUserInfo).flatMap(e -> ServerResponse.ok().bodyValue(e));
	}

	/**
	 * 刪除 User 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> deleteUser(ServerRequest request) {
		String userId = request.pathVariable("id");
		return userCommandService.deleteUserInfoById(Long.parseLong(userId))
				.flatMap(e -> ServerResponse.ok().bodyValue(e));
	}

}
