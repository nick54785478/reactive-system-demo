package com.example.demo.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.domain.service.RoleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Application Service
 * */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@AllArgsConstructor
public class RoleCommandService {

	RoleService roleService;

	/**
	 * 新增 角色資料
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<String> createRoleInfo(CreateRoleCommand command) {
		return roleService.createRole(command);
	}

	/**
	 * 更新 角色資料
	 * 
	 * @param command
	 * @return 成功訊息
	 */
	public Mono<String> updateRoleInfo(UpdateRoleCommand command) {
		return roleService.updateRole(command);
	}

	/**
	 * 刪除 角色資料
	 * 
	 * @param id
	 * @return 成功訊息
	 */
	public Mono<String> deleteRoleInfoById(Long id) {
		return roleService.deleteRole(id);
	}

}
