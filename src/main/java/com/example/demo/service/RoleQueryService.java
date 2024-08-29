package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.share.RoleInfoData;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Application Service
 */
@Service
@AllArgsConstructor
public class RoleQueryService {

	RoleService roleService;

	/**
	 * 取得角色清單
	 * 
	 * @return 角色清單 
	 */
	public Flux<RoleInfoData> getRoleList() {
		return roleService.getRoleList() // 取得 Flux<RoleInfo>
				// 透過 map 將其轉換為 RoleInfoData (非領域資料)
				.map(e -> BaseDataTransformer.transformData(e, RoleInfoData.class));
	}

	/**
	 * 透過 ID 取得角色資料
	 * 
	 * @param id
	 * @return 角色資料
	 */
	public Mono<RoleInfoData> getRoleById(Long id) {
		// 透過 map 將其轉換為 RoleInfoData (非領域資料)
		return roleService.getRoleById(id).map(e -> BaseDataTransformer.transformData(e, RoleInfoData.class));
	}

}
