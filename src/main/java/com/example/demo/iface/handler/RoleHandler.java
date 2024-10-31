package com.example.demo.iface.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.domain.share.RoleInfoData;
import com.example.demo.iface.dto.RoleCreatedResource;
import com.example.demo.iface.dto.RoleDeletedResource;
import com.example.demo.iface.dto.RoleInfoResource;
import com.example.demo.iface.dto.RoleUpdatedResource;
import com.example.demo.iface.dto.UserInfoResource;
import com.example.demo.service.RoleCommandService;
import com.example.demo.service.RoleQueryService;
import com.example.demo.util.BaseDataTransformer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@NoArgsConstructor
public class RoleHandler {

	@Autowired
	private RoleCommandService roleCommandService;
	@Autowired
	private RoleQueryService roleQueryService;

	/**
	 * 透過 RoleId 取得 Role 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> getRole(ServerRequest request) {
		String roleId = request.pathVariable("id");
		log.info("[Role Handler] method: GET, path:/api/vi/roles/{}", roleId);

		Mono<RoleInfoData> RoleMono = roleQueryService.getRoleById(Long.parseLong(roleId));
		return RoleMono.flatMap(
				role -> ServerResponse.ok().bodyValue(BaseDataTransformer.transformData(role, RoleInfoResource.class)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	/**
	 * 取得 Role 資料清單
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> getRoleList(ServerRequest request) {
		log.info("[Role Handler] method: GET, path:/api/vi/roles");
		Flux<RoleInfoData> RoleList = roleQueryService.getRoleList();
		return RoleList.hasElements().flatMap(hasElements -> {
			if (hasElements) {
				// 有值傳回
				return ServerResponse.ok().body(RoleList, RoleInfoResource.class);
			} else {
				// 無值傳回空 list
				return ServerResponse.noContent().build(); // 204 No Content
			}
		});
	}

	/**
	 * 新增 Role 資料
	 */
	public Mono<ServerResponse> createRole(ServerRequest request) {
		log.info("[Role Handler] method: POST, path:/api/vi/roles");
		Mono<CreateRoleCommand> RoleMono = request.bodyToMono(CreateRoleCommand.class);
		// flatMap 用於處理非同步流中的元素並將其對應到另一個 Publisher。
		return RoleMono.flatMap(roleCommandService::createRoleInfo)
				.flatMap(message -> ServerResponse.ok().bodyValue(new RoleCreatedResource(201, message)));
	}

	/**
	 * 更新 Role 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> updateRole(ServerRequest request) {
		Mono<UpdateRoleCommand> roleMono = request.bodyToMono(UpdateRoleCommand.class);
		log.info("[Role Handler] method: PUT, path:/api/vi/roles");
		return roleMono.flatMap(roleCommandService::updateRoleInfo)
				.flatMap(message -> ServerResponse.ok().bodyValue(new RoleUpdatedResource(200, message)));
	}

	/**
	 * 刪除 Role 資料
	 * 
	 * @param request
	 * @return 響應資料
	 */
	public Mono<ServerResponse> deleteRole(ServerRequest request) {
		String roleId = request.pathVariable("id");
		log.info("[Role Handler] method: DELETE, path:/api/vi/roles/{}", roleId);
		return roleCommandService.deleteRoleInfoById(Long.parseLong(roleId))
				.flatMap(message -> ServerResponse.ok().bodyValue(new RoleDeletedResource(200, message)));
	}

}
