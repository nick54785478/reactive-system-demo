package com.example.demo.domain.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.infra.repository.RoleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Domain Service
 */
@Slf4j
@Service
@AllArgsConstructor
public class RoleService {

	private RoleRepository roleRepository;

	/**
	 * 根據使用者ID 查詢使用者
	 * 
	 * @param id - 使用者代號
	 * @return Mono<User>
	 */
	public Mono<RoleInfo> getRoleById(Long id) {
		return roleRepository.findById(id);
	}

	/**
	 * 取得使用者清單
	 * 
	 * @return Flux<User>
	 */
	public Flux<RoleInfo> getRoleList() {
		return roleRepository.findAll();
	}

	/**
	 * 新增使用者資料
	 * 
	 * @param command
	 * @return Mono<User>
	 */
	public Mono<String> createRole(CreateRoleCommand command) {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.create(command);
		return roleRepository.save(roleInfo).map(e -> "成功新增一筆資料: " + e.getId());
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 * @return Mono<Integer> 更新資料筆數
	 */
	public Mono<String> updateRole(UpdateRoleCommand command) {
		return roleRepository.findById(command.getId()).flatMap(role -> {
			role.update(command);
			// 保存用戶
			return roleRepository.save(role);
		}).map(e -> "成功更新一筆資料, id:" + command.getId());

//		return userRepository.updateUserInfoById(command.getName(), command.getAddress(), command.getEmail(),
//				command.getId()).map(e -> "成功更新一筆資料, id:" + command.getId());
	}

	/**
	 * 刪除使用者資料
	 * 
	 * @param id - 使用者代號
	 * @return Mono<UserInfo>
	 */
	public Mono<String> deleteRole(Long id) {
		return roleRepository.findById(id).flatMap(role -> {
			// 更新 activeFlag = "N"
			role.delete();
			// 保存用戶
			return roleRepository.save(role);
		}).map(e -> "成功刪除一筆資料, id:" + e.getId());

	}

}
